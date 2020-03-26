package top.jplayer.networklibrary.net.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.jplayer.networklibrary.NetworkApplication;
import top.jplayer.networklibrary.net.cookie.CookieJarImpl;
import top.jplayer.networklibrary.net.cookie.OKhttpCookieChange;
import top.jplayer.networklibrary.net.interceptor.CommentHeaderInterceptor;
import top.jplayer.networklibrary.net.interceptor.CommentQueryInterceptor;
import top.jplayer.networklibrary.net.interceptor.CookieInterceptor;
import top.jplayer.networklibrary.net.interceptor.FixUrlInterceptor;
import top.jplayer.networklibrary.net.interceptor.JsonFixInterceptor;
import top.jplayer.networklibrary.net.interceptor.LoggerInterceptor;
import top.jplayer.networklibrary.net.interceptor.ProgressRequestInterceptor;
import top.jplayer.networklibrary.net.interceptor.ProgressResponseInterceptor;
import top.jplayer.networklibrary.net.interceptor.ResetHostInterceptor;
import top.jplayer.networklibrary.net.interceptor.SignHeaderInterceptor;
import top.jplayer.networklibrary.utils.GsonUtil;
import top.jplayer.networklibrary.utils.JNetLog;


/**
 * Created by Obl on 2018/1/12.
 * top.jplayer.baseprolibrary.net
 */

public class RetrofitManager {
    private static RetrofitManager mRetrofitManager;
    private Retrofit mRetrofit;
    private OkHttpClient.Builder mBuilder;

    public static synchronized RetrofitManager init() {

        if (mRetrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = new RetrofitManager();
                }
            }
        }
        return mRetrofitManager;
    }

    public void build(String baseUrl) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(FileConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonUtil.setGsonFilter()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mBuilder.build())
                .build();
    }

    @NonNull
    public RetrofitManager client(Context context) {
        client(context, NetworkApplication.TIME_OUT, interceptors());
        return this;
    }


    @NonNull
    public RetrofitManager client(Context context, List<Interceptor> interceptors) {
        client(context, NetworkApplication.TIME_OUT, interceptors);
        return this;
    }

    @SuppressLint("CheckResult")
    @NonNull
    public RetrofitManager client(Context context, Long timeOut, List<Interceptor> interceptors) {
        mBuilder = new OkHttpClient.Builder().connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cookieJar(new CookieJarImpl(new OKhttpCookieChange(context)));
        Observable.fromIterable(interceptors)
                .subscribe(interceptor -> mBuilder.addInterceptor(interceptor));
        return this;
    }

    /**
     * 默认添加的拦截器
     *
     */

    public static List<Interceptor> interceptors() {
        List<Interceptor> list = new ArrayList<>();
        list.add(new ResetHostInterceptor());
        list.add(new FixUrlInterceptor());
        list.add(new CommentQueryInterceptor());
        list.add(new CommentHeaderInterceptor());
        list.add(new SignHeaderInterceptor());
        list.add(new LoggerInterceptor());
        list.add(new ProgressRequestInterceptor());
        list.add(new JsonFixInterceptor());
        list.add(new CookieInterceptor());
        list.add(new ProgressResponseInterceptor());
        return list;
    }

    public <T> T create(Class<T> reqServer) throws NullPointerException {
        return mRetrofit.create(reqServer);
    }
}
