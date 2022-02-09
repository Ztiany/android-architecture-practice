package com.vclusters.cloud.main.home;

import com.app.base.app.ServiceProvider;
import com.vclusters.cloud.main.home.phone.data.CloudPhoneApi;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-09-02 15:37
 */
@Module
@InstallIn(ActivityRetainedComponent.class)
public class MainInjectionModule {

    @Provides
    static CloudPhoneApi provideCloudPhoneApi(ServiceProvider serviceProvider) {
        return serviceProvider.getDefault().create(CloudPhoneApi.class);
    }

}