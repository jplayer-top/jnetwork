package top.jplayer.jnetwork;

import androidx.appcompat.app.AppCompatActivity;
import top.jplayer.codelib.AutoWired;
import top.jplayer.jnetwork.presenter.MainPresenter;
import top.jplayer.networklibrary.contract.IContract;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements IContract.IView {
    @AutoWired
    public MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
