package me.ztiany.wan.account;

import com.app.common.api.router.Navigator;
import com.app.base.component.router.NavigatorKey;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import me.ztiany.wan.account.api.AccountModuleNavigator;

/**
 * @author Ztiany
 */
@Module
@InstallIn(SingletonComponent.class)
public class AccountModule {

    @Provides
    @Singleton
    @IntoMap
    @NavigatorKey(AccountModuleNavigator.class)
    public Navigator provideAccountModuleNavigator() {
        return new AccountModuleNavigatorImpl();
    }

}