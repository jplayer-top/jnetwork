package top.jplayer.jnetwork;

import androidx.appcompat.app.AppCompatActivity;
import top.jplayer.codelib.AutoWired;
import top.jplayer.codelib.AutoWiredBind;
import top.jplayer.codelib.IBind;
import top.jplayer.jnetwork.presenter.MainActivityPresenter;
import top.jplayer.jnetwork.presenter.SecondActivityPresenter;
import top.jplayer.networklibrary.NetworkApplication;
import top.jplayer.networklibrary.contract.IContract;
import top.jplayer.networklibrary.net.download.DownloadByManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements IContract.IView {
    @AutoWired
    public MainActivityPresenter mPresenter;
    private DownloadByManager mManager;
    private IBind mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(NetworkApplication.mHostMap);
        setContentView(R.layout.activity_main);
        mBind = AutoWiredBind.bind(this);
        mPresenter.getList("sdaasd");
    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
