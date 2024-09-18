package me.ztiany.wan.sample;

import com.android.base.core.AppLifecycle;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoSet;

/**
 * @author Ztiany
 */
@Module
@InstallIn(SingletonComponent.class)
public class SampleSingletonModule {

    @Provides
    @IntoSet
    public AppLifecycle provideSampleModuleInitializer(SampleModuleInitializer sampleModuleInitializer) {
        return sampleModuleInitializer;
    }

}