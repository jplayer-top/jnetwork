package top.jplayer.jnetwork;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import top.jplayer.codelib.AutoWiredBind;

/**
 * Created by Obl on 2020/6/1.
 * top.jplayer.jnetwork
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */
public  abstract  class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoWiredBind.bind(this);
    }
}
