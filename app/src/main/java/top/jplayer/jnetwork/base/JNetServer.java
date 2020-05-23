package top.jplayer.jnetwork.base;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import top.jplayer.codelib.AutoHost;
import top.jplayer.codelib.AutoMP;
import top.jplayer.networklibrary.Header$HOST;
import top.jplayer.networklibrary.model.bean.IResponseBean;

/**
 * Created by Obl on 2020/3/24.
 * top.jplayer.jnetwork.base
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public interface JNetServer {

    String HOST = "http://jplayer.top/";

    @AutoHost(key = "baidu")
    String BAI_DU_HOST = "http://www.baidu.com";

    @AutoHost(key = "google")
    String GOOGLE_HOST = "http://www.google.com";

    @AutoMP
    @Headers({Header$HOST.HEADER_BAI_DU_HOST})
    @GET("getList")
    Observable<IResponseBean> getList(@Query("test") String string);
}
