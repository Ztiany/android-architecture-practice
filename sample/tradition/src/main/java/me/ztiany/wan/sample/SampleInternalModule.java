package me.ztiany.wan.sample;

import static me.ztiany.wan.sample.common.net.SampleApiConfigurerKt.SAMPLE_HOST_FLAG;

import com.android.sdk.net.ServiceContext;
import com.app.common.api.network.ApiServiceFactoryProvider;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import me.ztiany.wan.sample.common.data.WanAndroidApi;

/**
 * @author Ztiany
 */
@Module
@InstallIn(ActivityRetainedComponent.class)
public class SampleInternalModule {

    @ActivityRetainedScoped
    @Provides
    static ServiceContext<WanAndroidApi> provideWanAndroidApi(ApiServiceFactoryProvider apiServiceFactoryProvider) {
        return apiServiceFactoryProvider.get(SAMPLE_HOST_FLAG).createServiceContext(WanAndroidApi.class);
    }

}