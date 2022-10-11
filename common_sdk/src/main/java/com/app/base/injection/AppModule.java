package com.app.base.injection;

import com.android.base.image.ImageLoader;
import com.android.base.image.ImageLoaderFactory;
import com.app.base.app.DefaultDispatcherProvider;
import com.app.base.app.DispatcherProvider;
import com.android.common.api.router.AppRouter;
import com.app.base.component.router.AppRouterImpl;
import com.android.common.api.services.AppServiceManager;
import com.app.base.component.services.AppServiceManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

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
    @Singleton
    AppRouter provideAppRouter() {
        return new AppRouterImpl();
    }

    @Provides
    @Singleton
    AppServiceManager provideAppServiceManager() {
        return new AppServiceManagerImpl();
    }

    @Provides
    @Singleton
    DispatcherProvider provideDispatcherProvider() {
        return new DefaultDispatcherProvider();
    }

}