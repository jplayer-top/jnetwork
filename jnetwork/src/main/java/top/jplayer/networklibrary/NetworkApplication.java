package top.jplayer.networklibrary;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Interceptor;
import top.jplayer.networklibrary.net.interceptor.LoggerInterceptor;
import top.jplayer.networklibrary.net.retrofit.RetrofitManager;

/**
 * Created by Administrator on 2019/7/16.
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public class NetworkApplication {

    private static NetworkApplication mInit;
    public static LinkedHashMap<String, String> mHeaderMap;
    public static LinkedHashMap<String, String> mUrlMap;
    public static WeakReference<Application> mWeakReference;

    public static final String resetHeader = "url_header_host";
    public static final Long TIME_OUT = 30L;

    private NetworkApplication(Application application) {
        mWeakReference = new WeakReference<>(application);
        mHeaderMap = new LinkedHashMap<>();
        mUrlMap = new LinkedHashMap<>();
        mHeaderMap.put("source", "android");
        mHeaderMap.put("version", BuildConfig.VERSION_NAME);
    }

    public synchronized static NetworkApplication with(Application application) {
        if (mInit == null) {

            synchronized (NetworkApplication.class) {
                if (mInit == null) {
                    mInit = new NetworkApplication(application);
                }
            }
        }
        return mInit;
    }

    public static Context getContext() {
        return mWeakReference.get().getApplicationContext();
    }

    /**
     * 初始化retrofit
     */
    public NetworkApplication retrofit() {
        RetrofitManager.init().client(mWeakReference.get()).build(BuildConfig.HOST);
        return this;
    }

    public NetworkApplication retrofit(String urlDefHost) {
        RetrofitManager.init().client(mWeakReference.get()).build(urlDefHost);
        return this;
    }


    public NetworkApplication retrofit(LoggerInterceptor.Logger logger) {
        RetrofitManager.init().client(mWeakReference.get(), logger).build(BuildConfig.HOST);
        return this;
    }

    public NetworkApplication retrofit(String urlDefHost, LoggerInterceptor.Logger logger) {
        RetrofitManager.init().client(mWeakReference.get(), logger).build(urlDefHost);
        return this;
    }

    public NetworkApplication retrofit(Interceptor... interceptors) {
        RetrofitManager.init().client(mWeakReference.get(), Arrays.asList(interceptors)).build(BuildConfig.HOST);
        return this;
    }

    public NetworkApplication retrofit(String urlDefHost, Interceptor... interceptors) {
        RetrofitManager.init().client(mWeakReference.get(), Arrays.asList(interceptors)).build(urlDefHost);
        return this;
    }


    /**
     * 添加单个url
     * key :设置的Header 值，value 设定的需要修改的 host
     * 使用：在注解上添加 @Header("url_heard_host:key")
     * 使用后该请求url会被代替
     */
    public NetworkApplication addUrl(String key, String value) {
        mUrlMap.put(key, value);
        return this;
    }

    /**
     * 添加集合url
     */
    public NetworkApplication urlMap(Map<String, String> map) {
        mUrlMap.putAll(map);
        return this;
    }


}
