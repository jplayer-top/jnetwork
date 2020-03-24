package top.jplayer.networklibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import top.jplayer.networklibrary.utils.LogUtil;

/**
 * Created by Obl on 2019/9/23.
 * top.jplayer.networklibrary.receiver
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public class ErrorLoginReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.e("-----ErrorLoginReceiver------");
    }
}
