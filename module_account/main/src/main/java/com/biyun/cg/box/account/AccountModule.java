package com.biyun.cg.box.account;

import com.app.common.api.router.AppNavigator;
import com.app.base.component.router.AppRouterKey;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import com.biyun.cg.box.account.api.AccountModuleNavigator;

/**
 * @author Ztiany
 */
@Module
@InstallIn(SingletonComponent.class)
public class AccountModule {

    @Provides
    @Singleton
    @IntoMap
    @AppRouterKey(AccountModuleNavigator.class)
    public AppNavigator provideAccountModuleNavigator() {
        return new AccountModuleNavigatorImpl();
    }

}