package com.app.base;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;

import com.android.base.app.BaseAppContext;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 10:20
 */
public abstract class AppContext extends BaseAppContext implements HasSupportFragmentInjector, HasActivityInjector {

    private static AppContext sAppContext;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
        initApp();
        configSDK();
        openDebugIfNeed();
    }

    private void openDebugIfNeed() {

    }

    private void configSDK() {

    }

    private void initApp() {
        injectAppContext();
    }

    public static AppContext getContext() {
        return sAppContext;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingFragmentInjector;
    }

    protected abstract void injectAppContext();

}
