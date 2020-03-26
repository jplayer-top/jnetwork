package top.jplayer.networklibrary.net.interceptor;

import android.content.Context;
import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import top.jplayer.networklibrary.NetworkApplication;
import top.jplayer.networklibrary.utils.JNetLog;
import top.jplayer.networklibrary.utils.SharePreUtil;

/**
 * Created by Obl on 2018/3/13.
 * top.jplayer.baseprolibrary.net.interceptor
 */

public class CookieInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Headers headers = response.headers();
        String cookie = headers.get("Set-Cookie");
        if (cookie != null && cookie.contains("efd4___ewei_shopv2_member_session_2")) {
            Context context = NetworkApplication.getContext();
            SharePreUtil.saveData(context, "cookie", cookie);
        }
        JNetLog.e(cookie);
        return response;
    }
}
