package me.ztiany.wan.main.injection;

import com.android.sdk.net.ServiceContext;
import com.app.common.api.network.ApiServiceFactoryProvider;

import me.ztiany.wan.main.data.HomeApi;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;

/**
 * @author Ztiany
 */
@Module
@InstallIn(ActivityRetainedComponent.class)
public class MainInternalModule {

    @Provides
    static ServiceContext<HomeApi> provideHomeApi(ApiServiceFactoryProvider apiServiceFactoryProvider) {
        return apiServiceFactoryProvider.getDefault().createServiceContext(HomeApi.class);
    }

}