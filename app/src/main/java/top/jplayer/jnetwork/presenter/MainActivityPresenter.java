package top.jplayer.jnetwork.presenter;

import top.jplayer.jnetwork.LogUtil;
import top.jplayer.jnetwork.MainActivity;
import top.jplayer.jnetwork.base.CommonPresenter$Auto;
import top.jplayer.networklibrary.model.bean.IResponseBean;
import top.jplayer.networklibrary.net.retrofit.DefaultCallBackObserver;

/**
 * Created by Obl on 2020/3/24.
 * top.jplayer.jnetwork.presenter
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public class MainActivityPresenter extends CommonPresenter$Auto<MainActivity> {
    public MainActivityPresenter(MainActivity iView) {
        super(iView);
    }

    public void getList(String string) {
        mModel.getList(string).subscribe(new DefaultCallBackObserver<IResponseBean>(this) {
            public void responseSuccess(top.jplayer.networklibrary.model.bean.IResponseBean bean) {
                //responseSuccess;
            }

            public void responseFail(top.jplayer.networklibrary.model.bean.IResponseBean bean) {
                //responseFail;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LogUtil.e(e.getMessage() + "-----------------------");
            }
        });
    }
}
