package top.jplayer.networklibrary;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import top.jplayer.networklibrary.net.interceptor.LoggerInterceptor;
import top.jplayer.networklibrary.net.retrofit.RetrofitManager;
import top.jplayer.networklibrary.utils.JNetLog;

/**
 * Created by Administrator on 2019/7/16.
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public class NetworkApplication {

    private static NetworkApplication mInit;
    public static LinkedHashMap<String, String> mHeaderMap;
    public static LinkedHashMap<String, String> mHostMap;
    public static WeakReference<Application> mWeakReference;
    public static String baseHost = "http://jplayer.top/";
    public static final String resetHeader = "url_header_host";
    public static Long TIME_OUT = 30L;

    private NetworkApplication(Application application) {
        mWeakReference = new WeakReference<>(application);
        mHeaderMap = new LinkedHashMap<>();
        mHostMap = new LinkedHashMap<>();
        mHeaderMap.put("source", "android");
        PackageManager packageManager = application.getPackageManager();
        String packageName = application.getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            mHeaderMap.put("version", packageInfo.versionName + "." + packageInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static NetworkApplication with(Application application) {
        if (mInit == null) {

            synchronized (NetworkApplication.class) {
                if (mInit == null) {
                    mInit = new NetworkApplication(application);
                    bindHeaderHost();
                }
            }
        }
        return mInit;
    }

    private static void bindHeaderHost() {
        try {
            Class.forName("top.jplayer.networklibrary.Header$HOST").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mWeakReference.get().getApplicationContext();
    }

    public NetworkApplication retrofit(String urlDefHost) {
        JNetLog.mLogListener = null;
        JNetLog.isDebug = true;
        retrofit(urlDefHost, null, true);
        return this;
    }


    public NetworkApplication retrofit(String urlDefHost, JNetLog.LogListener listener) {
        retrofit(urlDefHost, listener, true);
        return this;
    }

    public NetworkApplication retrofit(String urlDefHost, JNetLog.LogListener listener, boolean debug) {
        JNetLog.mLogListener = listener;
        JNetLog.isDebug = debug;
        RetrofitManager.init().client(mWeakReference.get()).build(urlDefHost);
        return this;
    }


    public NetworkApplication retrofit(String urlDefHost, JNetLog.LogListener listener, boolean debug, @NonNull Interceptor... interceptors) {
        JNetLog.mLogListener = listener;
        JNetLog.isDebug = debug;
        RetrofitManager.init().client(mWeakReference.get(), Arrays.asList(interceptors)).build(urlDefHost);
        return this;
    }

}
