package com.app.base.injection;

import com.app.common.api.usermanager.UserManager;
import com.app.base.component.errorhandler.AppErrorHandler;
import com.app.common.api.errorhandler.ErrorHandler;
import com.app.common.api.network.ServiceFactoryProvider;
import com.app.base.component.apifactory.ServiceFactoryProviderImpl;
import com.app.base.component.usermanager.UserManagerImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * @author Ztiany
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
    public ServiceFactoryProvider provideServiceFactory(ServiceFactoryProviderImpl serviceProvider) {
        return serviceProvider;
    }

    @Provides
    public ErrorHandler provideErrorHandler(AppErrorHandler appErrorHandler) {
        return appErrorHandler;
    }

}
