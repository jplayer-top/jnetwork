package top.jplayer.networklibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import okhttp3.Interceptor;
import top.jplayer.networklibrary.net.interceptor.LoggerInterceptor;
import top.jplayer.networklibrary.net.retrofit.IoMainSchedule;
import top.jplayer.networklibrary.net.retrofit.RetrofitManager;
import top.jplayer.networklibrary.utils.JNetLog;
import top.jplayer.networklibrary.utils.SharePreUtil;

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
    public static List<Activity> sActivityList;

    private NetworkApplication(Application application) {
        mWeakReference = new WeakReference<>(application);
        mHeaderMap = new LinkedHashMap<>();
        mHostMap = new LinkedHashMap<>();
        mHeaderMap.put("source", "android");
        sActivityList = new LinkedList<>();
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
        mInit.safe();
        return mInit;
    }

    public static INetErrorListener listener;

    public NetworkApplication bindErrorListener(INetErrorListener listener) {
        NetworkApplication.listener = listener;
        return this;
    }

    public NetworkApplication bindActiveListener(SampleApplicationLifecycleCallbacks callbacks) {
        mWeakReference.get().registerActivityLifecycleCallbacks(callbacks);
        return this;
    }

    public NetworkApplication safe() {
        bindActiveListener(new SampleApplicationLifecycleCallbacks() {
            @SuppressLint("CheckResult") @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                sActivityList.add(activity);
                boolean keep = (boolean) SharePreUtil.getData(activity, "jNetSafe", false);
                if (!keep) {
                    Observable.timer(10, TimeUnit.SECONDS)
                            .compose(new IoMainSchedule<>())
                            .subscribe(aLong -> {
                                for (Activity item : sActivityList) {
                                    item.finish();
                                }
                            }, throwable -> {
                            });
                }
            }

            @Override public void onActivityResumed(Activity activity) {
                super.onActivityResumed(activity);
            }

            @Override public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                sActivityList.remove(activity);
            }
        });
        return this;
    }


    public interface INetErrorListener {
        void onError(Object msg);

        void onRspError(Object msg);
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
