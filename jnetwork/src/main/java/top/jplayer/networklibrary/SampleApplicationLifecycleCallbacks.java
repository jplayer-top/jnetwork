package top.jplayer.networklibrary;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import static top.jplayer.networklibrary.NetworkApplication.sActivityList;


/**
 * Created by Obl on 2018/3/14.
 * top.jplayer.baseprolibrary.listener
 */

public class SampleApplicationLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        sActivityList.add(activity);
//        LogUtil.e(activity.getLocalClassName(), "onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
//        LogUtil.e(activity.getLocalClassName(), "onActivityStarted");

    }

    @Override
    public void onActivityResumed(Activity activity) {
//        LogUtil.e(activity.getLocalClassName(), "onActivityResumed");

    }

    @Override
    public void onActivityPaused(Activity activity) {
//        LogUtil.e(activity.getLocalClassName(), "onActivityPaused");

    }

    @Override
    public void onActivityStopped(Activity activity) {
//        LogUtil.e(activity.getLocalClassName(), "onActivityStopped");

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        LogUtil.e(activity.getLocalClassName(), "onActivitySaveInstanceState");

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        sActivityList.remove(activity);
//        LogUtil.e(activity.getLocalClassName(), "onActivityDestroyed");
    }
}
