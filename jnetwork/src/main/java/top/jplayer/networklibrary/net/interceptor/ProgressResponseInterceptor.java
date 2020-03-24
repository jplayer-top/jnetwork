package top.jplayer.networklibrary.net.interceptor;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import top.jplayer.networklibrary.NetworkApplication;
import top.jplayer.networklibrary.net.retrofit.ProgressResponseBody;
import top.jplayer.networklibrary.net.retrofit.ProgressUpDownListener;

/**
 * Created by Administrator on 2018/1/26.
 * 下载进度
 */

public class ProgressResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request requestHeader = chain.request();
        Response requestBody = chain.proceed(chain.request());
        List<String> headerValues = requestHeader.headers("download");
        final boolean isDownload = headerValues != null && headerValues.contains("download");
        ProgressResponseBody body = new ProgressResponseBody(requestBody.body(), new ProgressUpDownListener() {
            @Override
            public void onResponseProgress(long progress, long total, boolean done) {
                if (isDownload) {
                    Context context = NetworkApplication.getContext();
                    Intent intent = new Intent();
                    intent.setAction(context.getPackageName() + ".download");
                    intent.putExtra("total", total / 1024 / 1024);
                    intent.putExtra("progress", progress / 1024 / 1024);
                    context.sendBroadcast(intent);
                }
            }

            @Override
            public void onRequestProgress(long progress, long total, boolean done) {

            }
        });
        return requestBody.newBuilder()
                .body(body)
                .build();
    }
}
