package com.app.base.injection;

import com.app.base.app.AppErrorHandler;
import com.app.base.app.ErrorHandler;
import com.app.base.app.ServiceProvider;
import com.app.base.app.ServiceProviderImpl;
import com.app.base.services.devicemanager.DeviceManager;
import com.app.base.services.devicemanager.DeviceManagerImpl;
import com.app.base.services.usermanager.UserManager;
import com.app.base.services.usermanager.UserManagerImpl;

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
    public DeviceManager provideDeviceManager(DeviceManagerImpl deviceManager) {
        return deviceManager;
    }

    @Provides
    public ServiceProvider provideServiceFactory(ServiceProviderImpl serviceProvider) {
        return serviceProvider;
    }

    @Provides
    public ErrorHandler provideErrorHandler(AppErrorHandler appErrorHandler) {
        return appErrorHandler;
    }

}
