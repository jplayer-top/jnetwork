package top.jplayer.jnetwork;

import androidx.appcompat.app.AppCompatActivity;
import top.jplayer.codelib.AutoWired;
import top.jplayer.jnetwork.presenter.SecondActivityPresenter;
import top.jplayer.networklibrary.contract.IContract;

/**
 * Created by Obl on 2020/6/1.
 * top.jplayer.jnetwork
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class SecondActivity extends BaseActivity implements IContract.IView {
    @AutoWired
    public SecondActivityPresenter mTestPresenter;


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
