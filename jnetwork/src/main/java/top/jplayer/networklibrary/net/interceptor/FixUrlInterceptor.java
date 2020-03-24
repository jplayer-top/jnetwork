package top.jplayer.networklibrary.net.interceptor;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import top.jplayer.networklibrary.BuildConfig;
import top.jplayer.networklibrary.NetworkApplication;
import top.jplayer.networklibrary.utils.LogUtil;

import static top.jplayer.networklibrary.NetworkApplication.resetHeader;

/**
 * Created by Administrator on 2018/1/26.
 * 动态设置 Url
 */

public class FixUrlInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        HttpUrl oldHttpUrl = request.url();
        String path = oldHttpUrl.encodedPath();
        List<String> pathSegments = oldHttpUrl.pathSegments();
        LogUtil.e(oldHttpUrl);
        if (BuildConfig.HOST.contains(oldHttpUrl.host())) {
            //重建新的HttpUrl，修改需要修改的url部分
            HttpUrl newFullUrl = oldHttpUrl
                    .newBuilder()
                    .scheme(oldHttpUrl.scheme())
                    .host(oldHttpUrl.host())
                    .port(oldHttpUrl.port())
                    .build();
            return chain.proceed(builder.url(newFullUrl).build());
        } else {
            return chain.proceed(request);
        }
    }
}
