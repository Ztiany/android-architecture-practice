package com.app.base.injection;

import com.android.common.api.usermanager.UserManager;
import com.app.base.app.AppErrorHandler;
import com.app.base.app.ErrorHandler;
import com.android.common.api.network.ApiServiceFactoryProvider;
import com.app.base.app.ApiServiceFactoryProviderImpl;
import com.app.base.component.usermanager.UserManagerImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:06
 */
@Module
@InstallIn(SingletonComponent.class)
public class DataModule {

    @Provides
    public UserManager provideUserDataSource(UserManagerImpl userManager) {
        return userManager;
    }

    @Provides
    public ApiServiceFactoryProvider provideServiceFactory(ApiServiceFactoryProviderImpl serviceProvider) {
        return serviceProvider;
    }

    @Provides
    public ErrorHandler provideErrorHandler(AppErrorHandler appErrorHandler) {
        return appErrorHandler;
    }

}
