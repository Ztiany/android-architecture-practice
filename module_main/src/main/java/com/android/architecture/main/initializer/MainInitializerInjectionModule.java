package com.android.architecture.main.initializer;

import com.android.base.architecture.app.AppLifecycle;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoSet;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-09-02 15:37
 */
@Module
@InstallIn(SingletonComponent.class)
public class MainInitializerInjectionModule {

    @Provides
    @IntoSet
    public AppLifecycle provideMainModuleInitializer(MainModuleInitializer mainModuleInitializer) {
        return mainModuleInitializer;
    }

}