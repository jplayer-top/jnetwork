package top.jplayer.networklibrary.net.retrofit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Obl on 2018/1/17.
 * top.jplayer.baseprolibrary.net
 */

public class DelayIoMainSchedule<T> extends BaseSchedule<T> {
    public DelayIoMainSchedule() {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(upSchedule)
                .observeOn(downSchedule);
    }
}
