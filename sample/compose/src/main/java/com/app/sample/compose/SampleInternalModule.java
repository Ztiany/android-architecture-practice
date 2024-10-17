package com.app.sample.compose;


import static com.app.sample.compose.net.SampleApiConfigurerKt.SAMPLE_HOST_FLAG;

import com.android.sdk.net.ServiceContext;
import com.app.common.api.network.ServiceFactoryProvider;
import com.app.sample.compose.data.WanAndroidApi;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

/**
 * @author Ztiany
 */
@Module
@InstallIn(ActivityRetainedComponent.class)
public class SampleInternalModule {

    @ActivityRetainedScoped
    @Provides
    static ServiceContext<WanAndroidApi> provideWanAndroidApi(ServiceFactoryProvider apiServiceFactoryProvider) {
        return apiServiceFactoryProvider.get(SAMPLE_HOST_FLAG).createServiceContext(WanAndroidApi.class);
    }

}