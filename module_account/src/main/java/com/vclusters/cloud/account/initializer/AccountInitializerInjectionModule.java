package com.vclusters.cloud.account.initializer;

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
public class AccountInitializerInjectionModule {

    @Provides
    @IntoSet
    public AppLifecycle provideMainModuleInitializer(AccountModuleInitializer accountModuleInitializer) {
        return accountModuleInitializer;
    }

}