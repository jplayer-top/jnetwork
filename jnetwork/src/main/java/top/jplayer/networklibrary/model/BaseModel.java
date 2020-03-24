package top.jplayer.networklibrary.model;


import top.jplayer.networklibrary.net.retrofit.RetrofitManager;

/**
 * Created by Obl on 2018/7/6.
 * top.jplayer.baseprolibrary.mvp.model
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class BaseModel<T> {

    public final T mServer;

    public BaseModel(Class<T> t) {
        mServer = RetrofitManager.init().create(t);
    }
}
