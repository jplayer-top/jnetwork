package top.jplayer.networklibrary.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Obl on 2020/3/26.
 * top.jplayer.networklibrary.utils
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public class JNetLog {
    public interface LogListener {
        void e(Object... msg);
    }

    public static LogListener mLogListener = null;
    public static boolean isDebug = true;

    public static void e(Object... msg) {
        if (isDebug) {
            if (mLogListener != null) {
                mLogListener.e(msg);
            } else {
                ArrayList<String> strings = new ArrayList<>();
                for (Object o : msg) {
                    strings.add(o.toString());
                }
                String json = new Gson().toJson(strings);
                Log.e("JNet", json);
            }
        }
    }

    public static void e(Object msg) {

        if (isDebug) {

            if (mLogListener != null) {
                mLogListener.e(msg);
            } else {
                try {
                    if (msg instanceof String) {
                        Log.e("JNet", msg.toString());
                    } else {
                        String json = new Gson().toJson(msg);
                        Log.e("JNet", json);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("JNet", "解析异常-------------");
                }
            }
        }
    }
}
