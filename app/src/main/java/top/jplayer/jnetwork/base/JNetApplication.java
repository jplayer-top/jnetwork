package top.jplayer.jnetwork.base;

import android.app.Application;
import android.util.Log;

import top.jplayer.jnetwork.LogUtil;
import top.jplayer.networklibrary.NetworkApplication;
import top.jplayer.networklibrary.utils.JNetLog;

/**
 * Created by Obl on 2020/3/24.
 * top.jplayer.jnetwork.base
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public class JNetApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkApplication.with(this).retrofit(JNetServer.HOST).bindErrorListener(LogUtil::e);
    }
}
