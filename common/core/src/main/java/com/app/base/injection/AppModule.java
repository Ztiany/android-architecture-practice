package com.app.base.injection;

import com.android.base.image.ImageLoader;
import com.android.base.image.ImageLoaderFactory;
import com.app.common.api.router.AppRouter;
import com.app.common.api.router.AppNavigator;
import com.app.common.api.appservice.AppService;
import com.app.common.api.appservice.AppServiceManager;
import com.app.base.app.DefaultDispatcherProvider;
import com.app.base.app.DispatcherProvider;
import com.app.base.component.router.AppRouterImpl;
import com.app.base.component.router.AppRouterKey;
import com.app.base.component.appservice.AppServiceKey;
import com.app.base.component.appservice.AppServiceManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 10:33
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    ImageLoader provideImageLoader() {
        return ImageLoaderFactory.getImageLoader();
    }

    @Provides
    AppRouter provideAppRouter(AppRouterImpl appRouter) {
        return appRouter;
    }

    @Provides
    AppServiceManager provideAppServiceManager(AppServiceManagerImpl appServiceManager) {
        return appServiceManager;
    }

    @Provides
    @Singleton
    DispatcherProvider provideDispatcherProvider() {
        return new DefaultDispatcherProvider();
    }

    @Provides
    @Singleton
    @IntoMap
    @AppServiceKey(DummyAppService.class)
    public AppService provideDummyAppService() {
        return new DummyAppServiceImpl();
    }

    @Provides
    @Singleton
    @IntoMap
    @AppRouterKey(DummyNavigator.class)
    public AppNavigator provideDummyNavigator() {
        return new DummyNavigatorImpl();
    }

}