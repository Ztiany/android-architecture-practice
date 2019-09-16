package com.app.base.app;

import androidx.fragment.app.Fragment;

import com.android.base.app.dagger.Injectable;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2018-11-01
 */
public abstract class InjectorAppBaseActivity extends AppBaseActivity implements Injectable, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> mAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mAndroidInjector;
    }

}
