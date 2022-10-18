package me.ztiany.architecture.main;

import com.android.base.architecture.app.AppLifecycle;
import com.android.common.api.router.AppNavigator;
import com.app.base.component.router.AppRouterKey;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import me.ztiany.architecture.main.api.MainModuleNavigator;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-09-02 15:37
 */
@Module
@InstallIn(SingletonComponent.class)
public class MainModule {

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