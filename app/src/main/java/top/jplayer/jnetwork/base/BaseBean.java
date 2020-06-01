package top.jplayer.jnetwork.base;

import top.jplayer.networklibrary.model.bean.IResponseBean;

/**
 * Created by Obl on 2020/3/26.
 * top.jplayer.jnetwork.base
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public class BaseBean implements IResponseBean {
    /**
     * result : ok
     * data : {"timeStr":"2020-03-26 16:57:03","time":1585213023643}
     * message : 操作成功
     * curson : null
     * erros : null
     */

    public String result;
    public DataBean data;
    public String message;
    public Object curson;
    public Object erros;

    @Override
    public boolean rspSuccess() {
        return "ok".equals(result);
    }

    @Override
    public String rspMsg() {
        return message;
    }

    @Override
    public String rspCode() {
        return result;
    }

    public static class DataBean {
        /**
         * timeStr : 2020-03-26 16:57:03
         * time : 1585213023643
         */

        public String timeStr;
        public long time;
    }
}
