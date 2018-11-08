package me.ztiany.architecture.home;


import com.android.base.app.dagger.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 18:07
 */
@Module
public abstract class TestBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector
    public abstract HomeDebugActivity bindHomeDebugActivity();

}
