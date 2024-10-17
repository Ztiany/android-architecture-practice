package me.ztiany.wan.account.injection;

import com.app.base.component.router.NavigatorKey;
import com.app.common.api.router.Navigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import me.ztiany.wan.account.AccountModuleNavigatorImpl;
import me.ztiany.wan.account.api.AccountModuleNavigator;

/**
 * @author Ztiany
 */
@Module
@InstallIn(SingletonComponent.class)
public class AccountSingletonModule {

    @Provides
    @Singleton
    @IntoMap
    @NavigatorKey(AccountModuleNavigator.class)
    public Navigator provideAccountModuleNavigator() {
        return new AccountModuleNavigatorImpl();
    }

}