package me.ztiany.wan.###template###.injection;

import com.android.sdk.net.ServiceContext;
import com.app.common.api.network.ServiceFactoryProvider;

import me.ztiany.wan.###template###.data.$$$template$$$Api;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;

@Module
@InstallIn(ActivityRetainedComponent.class)
public class $$$template$$$InternalModule {

    @Provides
    static ServiceContext<$$$template$$$Api> provide$$$template$$$Api(ServiceFactoryProvider serviceFactoryProvider) {
        return serviceFactoryProvider.getDefault().createServiceContext($$$template$$$Api.class);
    }

}