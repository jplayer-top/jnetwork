package top.jplayer.jnetwork;

import androidx.appcompat.app.AppCompatActivity;
import top.jplayer.codelib.AutoWired;
import top.jplayer.codelib.AutoWiredBind;
import top.jplayer.jnetwork.presenter.MainPresenter;
import top.jplayer.networklibrary.NetworkApplication;
import top.jplayer.networklibrary.contract.IContract;
import top.jplayer.networklibrary.net.download.DownloadByManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements IContract.IView {
    @AutoWired
    public MainPresenter mMainPresenter;
    private DownloadByManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoWiredBind.bind(this);
        mMainPresenter.getList("sdaasd");
        LogUtil.e(NetworkApplication.mHostMap);
        mManager = new DownloadByManager(this);
//        mManager.bind(100, "asdasdas", "http://jplayer.top/app-release.apk")
//                .download()
//                .listener(new DownloadByManager.OnDownloadListener() {
//                    @Override
//                    public void update(int currentByte, int totalByte) {
//                        LogUtil.e(currentByte + "/", totalByte + "");
//                    }
//
//                    @Override
//                    public void error(String msg) {
//                        LogUtil.e(msg);
//                    }
//                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mManager.onPause();
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
}
