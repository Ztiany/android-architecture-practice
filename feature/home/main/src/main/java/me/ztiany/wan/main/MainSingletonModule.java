package me.ztiany.wan.main;

import com.android.base.core.AppLifecycle;
import com.app.base.component.router.NavigatorKey;
import com.app.common.api.router.Navigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;

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
    @NavigatorKey(MainModuleNavigator.class)
    public Navigator provideMainModuleNavigator() {
        return new MainModuleNavigatorImpl();
    }

}