package top.jplayer.networklibrary.net.retrofit;

/**
 * Created by Obl on 2018/8/7.
 * top.jplayer.baseprolibrary.net.retrofit
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public interface ProgressUpDownListener {

    void onResponseProgress(final long progress, final long total, final boolean done);

    void onRequestProgress(final long progress, final long total, final boolean done);

}
