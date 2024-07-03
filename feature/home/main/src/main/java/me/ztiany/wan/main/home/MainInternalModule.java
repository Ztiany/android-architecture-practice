package me.ztiany.wan.main.home;

import com.android.sdk.net.ServiceContext;
import com.app.common.api.network.ApiServiceFactoryProvider;

import me.ztiany.wan.main.home.feed.data.FeedApi;

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
    static ServiceContext<FeedApi> provideFeedApi(ApiServiceFactoryProvider apiServiceFactoryProvider) {
        return apiServiceFactoryProvider.getDefault().createServiceContext(FeedApi.class);
    }

}