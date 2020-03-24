package top.jplayer.networklibrary.model.bean;

import android.text.TextUtils;

/**
 * Created by Obl on 2018/1/25.
 * com.modiwu.mah.mvp.model.bean
 */

public interface IResponseBean {

    /**
     * 加载网络数据成功
     */
    boolean rspSuccess();

    /**
     * 提示内容
     */
    String rspMsg();

    /**
     * 状态码
     */
    String rspCode();

}
