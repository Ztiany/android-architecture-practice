package com.app.base.app;

import com.android.base.app.dagger.Injectable;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2018-11-01
 */
public abstract class InjectorAppBaseActivity extends AppBaseActivity implements Injectable, HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }

}
