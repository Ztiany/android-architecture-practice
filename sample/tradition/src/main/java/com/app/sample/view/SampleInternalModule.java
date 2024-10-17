package com.app.sample.view;

import static com.app.sample.view.common.net.SampleApiConfigurerKt.SAMPLE_HOST_FLAG;

import com.android.sdk.net.ServiceContext;
import com.app.common.api.network.ServiceFactoryProvider;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import com.app.sample.view.common.data.WanAndroidApi;

/**
 * @author Ztiany
 */
@Module
@InstallIn(ActivityRetainedComponent.class)
public class SampleInternalModule {

    @ActivityRetainedScoped
    @Provides
    static ServiceContext<WanAndroidApi> provideWanAndroidApi(ServiceFactoryProvider serviceFactoryProvider) {
        return serviceFactoryProvider.get(SAMPLE_HOST_FLAG).createServiceContext(WanAndroidApi.class);
    }

}