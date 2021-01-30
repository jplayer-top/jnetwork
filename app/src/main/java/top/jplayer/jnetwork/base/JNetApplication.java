package top.jplayer.jnetwork.base;

import android.app.Application;

import top.jplayer.networklibrary.NetworkApplication;

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
        NetworkApplication.with(this).retrofit(JNServer.HOST).bindErrorListener(new NetworkApplication.INetErrorListener() {
            @Override public void onError(Object msg) {

            }

            @Override public void onRspError(Object msg) {

            }
        });
    }
}
