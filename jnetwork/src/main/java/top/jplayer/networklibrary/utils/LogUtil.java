package top.jplayer.networklibrary.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import top.jplayer.networklibrary.BuildConfig;

/**
 * Created by Obl on 2018/1/13.
 * top.jplayer.baseprolibrary.utils
 */

public class LogUtil {
    public static void net(Object o) {
        if (BuildConfig.debug) {
            if (o instanceof String) {
                Log.e("Obl-Net", o.toString());
                return;
            }
            Log.e("Obl-Net", new Gson().toJson(o));
        }
    }

    public static void e(Object o) {

        if (BuildConfig.debug) {
            if (o instanceof String) {
                Log.e("Obl--Log", o.toString());
                return;
            }
            Log.e("Obl--Log", new Gson().toJson(o));
        }
    }

    public static void e(Object... o) {

        if (BuildConfig.debug) {
            List<Object> objects = Arrays.asList(o);
            Log.e("Obl-Log", new Gson().toJson(objects));
        }
    }

    public static void o(Object o) {

        if (BuildConfig.debug) {
            Log.e("Obl-Log", o.toString());
        }
    }

    public static String str(Object o) {

        if (BuildConfig.debug) {
            if (o instanceof String) {
                Log.e("Obl-Log", o.toString());
                return o.toString();
            }
            String json = new Gson().toJson(o);
            Log.e("Obl-Log", json);
            return json;
        }
        return "";
    }

    public static void method() {
        int level = 1;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String methodName = stacks[level].getMethodName();
        e(methodName);
    }

}
