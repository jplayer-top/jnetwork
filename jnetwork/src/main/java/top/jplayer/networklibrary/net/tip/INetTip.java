package top.jplayer.networklibrary.net.tip;

/**
 * Created by PEO on 2017/4/10.
 */

public interface INetTip {
    void tipStart();

    void tipEnd();

    void tipComplete();

    void tipSuccess(String msg);

    void tipFail(String code, String msg);

    void tipError(Throwable t);
}
