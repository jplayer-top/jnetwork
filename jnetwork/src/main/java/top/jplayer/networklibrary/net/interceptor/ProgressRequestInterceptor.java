package top.jplayer.networklibrary.net.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.jplayer.networklibrary.net.retrofit.ProgressRequestBody;
import top.jplayer.networklibrary.net.retrofit.ProgressUpDownListener;
import top.jplayer.networklibrary.utils.JNetLog;

/**
 * Created by Administrator on 2018/1/26.
 * 上传进度
 */

public class ProgressRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        RequestBody body = original.body();
        if (body instanceof MultipartBody) {
            Request request = original.newBuilder()
                    .method(original.method(), new ProgressRequestBody(body, new ProgressUpDownListener() {
                        @Override
                        public void onResponseProgress(long progress, long total, boolean done) {

                        }

                        @Override
                        public void onRequestProgress(long progress, long total, boolean done) {
                            JNetLog.e("NetWork", progress + "/" + total);
                        }
                    }))
                    .build();
            return chain.proceed(request);
        }
        return chain.proceed(original);
    }
}
