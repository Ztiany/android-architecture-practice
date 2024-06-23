package me.ztiany.wan.sample.injection;

import static me.ztiany.wan.sample.SampleApiConfigurerKt.SAMPLE_HOST_FLAG;

import com.android.sdk.net.ServiceContext;
import com.app.common.api.network.ApiServiceFactoryProvider;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import me.ztiany.wan.sample.data.SampleApi;

/**
 * @author Ztiany
 */
@Module
@InstallIn(ActivityRetainedComponent.class)
public class SampleInternalModule {

    @Provides
    static ServiceContext<SampleApi> provideSampleApi(ApiServiceFactoryProvider apiServiceFactoryProvider) {
        return apiServiceFactoryProvider.get(SAMPLE_HOST_FLAG).createServiceContext(SampleApi.class);
    }

}