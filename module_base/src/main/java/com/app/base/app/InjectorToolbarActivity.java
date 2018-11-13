package com.app.base.app;

import android.support.v4.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2018-11-02 14:57
 */
public abstract class InjectorToolbarActivity extends ToolBarActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> mAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mAndroidInjector;
    }

    @Override
    protected boolean hasInjector() {
        return true;
    }

}