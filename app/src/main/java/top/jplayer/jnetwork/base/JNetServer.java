package top.jplayer.jnetwork.base;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import top.jplayer.codelib.AutoHost;
import top.jplayer.codelib.AutoMP;
import top.jplayer.jnetwork.pojo.EmptyPojo;
import top.jplayer.networklibrary.model.bean.IResponseBean;

/**
 * Created by Obl on 2020/3/24.
 * top.jplayer.jnetwork.base
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public interface JNetServer {

    String HOST = "https://api.hicmall.com.cn/api/server/";

    @AutoHost(key = "baidu")
    String BAI_DU_HOST = "http://www.baidu.com";

    @AutoHost(key = "google")
    String GOOGLE_HOST = "http://www.google.com";

    @AutoMP
    @POST("customer/uncheck1/time")
    Observable<BaseBean> getCurTime(@Body() EmptyPojo pojo);
}
