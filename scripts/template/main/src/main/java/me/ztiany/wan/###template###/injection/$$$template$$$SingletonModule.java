package me.ztiany.wan.###template###.injection;

import com.app.base.component.router.NavigatorKey;
import com.app.common.api.router.Navigator;

import me.ztiany.wan.###template###.$$$template$$$ModuleNavigatorImpl;
import me.ztiany.wan.###template###.api.$$$template$$$ModuleNavigator;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;

@Module
@InstallIn(SingletonComponent.class)
public class $$$template$$$SingletonModule {

    @Provides
    @IntoMap
    @NavigatorKey($$$template$$$ModuleNavigator.class)
    public Navigator provide$$$template$$$ModuleNavigator($$$template$$$ModuleNavigatorImpl ###template###ModuleNavigator) {
        return ###template###ModuleNavigator;
    }

}