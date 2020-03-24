package top.jplayer.networklibrary.net.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import top.jplayer.networklibrary.NetworkApplication;

import static top.jplayer.networklibrary.NetworkApplication.resetHeader;

/**
 * Created by Administrator on 2018/1/26.
 * 动态设置 Url
 */

public class ResetHostInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Map<String, String> urlMap = NetworkApplication.mUrlMap;
        //获取request
        Request request = chain.request();
        //获取request的创建者builder
        Request.Builder builder = request.newBuilder();
        //从request中获取 指定 headers，通过给定的键url_name
        List<String> headerValues = request.headers(resetHeader);
        if (headerValues != null && headerValues.size() > 0) {
            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用,匹配获得新的BaseUrl
            String headerValue = headerValues.get(0);
            HttpUrl oldHttpUrl = request.url();
            HttpUrl newBaseUrl = oldHttpUrl;

            if (urlMap.containsKey(headerValue)) {
                newBaseUrl = HttpUrl.parse(urlMap.get(headerValue));
            }
            assert newBaseUrl != null;
            //重建新的HttpUrl，修改需要修改的url部分
            HttpUrl newFullUrl = oldHttpUrl
                    .newBuilder()
                    .scheme(newBaseUrl.scheme())
                    .host(newBaseUrl.host())
                    .port(newBaseUrl.port())
                    .build();
            //重建这个request，通过builder.url(newFullUrl).retrofit()；
            //然后返回一个response至此结束修改
            return chain.proceed(builder.url(newFullUrl).build());
        } else {
            return chain.proceed(request);
        }
    }
}
