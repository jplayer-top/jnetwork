package top.jplayer.codelib;

/**
 * Created by Obl on 2020/3/23.
 * top.jplayer.codelib
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public abstract class IBind<T> {
    public T targer;

    public IBind(T t) {
        this.targer = t;
    }

    public abstract void unbind();
}
