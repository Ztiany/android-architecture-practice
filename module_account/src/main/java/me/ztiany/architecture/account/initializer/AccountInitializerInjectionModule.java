package me.ztiany.architecture.account.initializer;

import com.android.common.api.router.AppNavigator;
import com.app.base.component.router.AppRouterKey;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import me.ztiany.architecture.account.api.AccountModuleNavigator;
import me.ztiany.architecture.account.apiimpl.AccountModuleNavigatorImpl;

/**
 * @author Ztiany
 */
@Module
@InstallIn(SingletonComponent.class)
public class AccountInitializerInjectionModule {

    @Provides
    @Singleton
    @IntoMap
    @AppRouterKey(AccountModuleNavigator.class)
    public AppNavigator provideAccountModuleNavigator() {
        return new AccountModuleNavigatorImpl();
    }

}