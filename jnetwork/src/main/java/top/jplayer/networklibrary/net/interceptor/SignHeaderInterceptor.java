package top.jplayer.networklibrary.net.interceptor;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.Nullable;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import top.jplayer.networklibrary.NetworkApplication;
import top.jplayer.networklibrary.utils.JNetLog;
import top.jplayer.networklibrary.utils.MD5Utils;

import static top.jplayer.networklibrary.net.interceptor.LoggerInterceptor.isPlaintext;

/**
 * Created by Obl on 2018/3/13.
 * top.jplayer.baseprolibrary.net.interceptor
 * 设置公共头信息 例如 token,cookie
 */

public class SignHeaderInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(@Nullable Chain chain) throws IOException {
        assert chain != null;
        Request request = chain.request();
        RequestBody body = request.body();

        String timestamp = Long.toString(new Date().getTime());
        boolean hasRequestBody = body != null;

        Request.Builder newBuilder = request.newBuilder();

        StringBuilder builder = new StringBuilder();
        if (hasRequestBody) {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (isPlaintext(buffer)) {
                String message = buffer.readString(charset);
                if (!"".equals(message)) {
                    JNetLog.e("----动态Header-Sign添加--");
                    Gson gson = new Gson();
                    TreeMap<String, Object> map = gson.fromJson(message, new TypeToken<TreeMap<String, Object>>() {
                    }.getType());
                    map.put("source", "android");
                    map.put("timestamp", timestamp);

                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        builder.append(entry.getKey());
                        builder.append("=");
                        builder.append(entry.getValue());
                        builder.append("&");
                    }
                   JNetLog.e("---Pre-Md5---");
                   JNetLog.e(builder.toString());
                   JNetLog.e("------");
                }
            } else {
                JNetLog.e("--> WillAdd " + request.method() + " (binary "
                        + body.contentLength() + "-byte body omitted)");
            }
        }
        NetworkApplication.mHeaderMap.put("timestamp", timestamp);
        String md5 = MD5Utils.getMD5(builder.toString());
        NetworkApplication.mHeaderMap.put("sign", md5.toUpperCase());

        Request build = newBuilder
                .headers(Headers.of(NetworkApplication.mHeaderMap))
                .method(request.method(), body)
                .build();
        return chain.proceed(build);
    }
}
