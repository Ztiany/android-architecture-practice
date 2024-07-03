package me.ztiany.wan.main.home;

import com.android.base.core.AppLifecycle;
import com.app.base.component.router.AppRouterKey;
import com.app.common.api.router.AppNavigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import me.ztiany.wan.main.home.MainModuleInitializer;
import me.ztiany.wan.main.MainModuleNavigator;
import me.ztiany.wan.main.home.MainModuleNavigatorImpl;

/**
 * @author Ztiany
 */
@Module
@InstallIn(SingletonComponent.class)
public class MainSingletonModule {

    @Provides
    @IntoSet
    public AppLifecycle provideMainModuleInitializer(MainModuleInitializer mainModuleInitializer) {
        return mainModuleInitializer;
    }

    @Provides
    @Singleton
    @IntoMap
    @AppRouterKey(MainModuleNavigator.class)
    public AppNavigator provideMainModuleNavigator() {
        return new MainModuleNavigatorImpl();
    }

}