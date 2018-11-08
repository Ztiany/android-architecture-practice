package com.app.base.di;

import android.content.Context;

import com.android.base.app.dagger.ContextType;
import com.android.sdk.cache.CacheFactory;
import com.android.sdk.cache.CacheFactoryImpl;
import com.android.sdk.cache.CacheManager;
import com.android.sdk.cache.MMKVCacheImpl;
import com.android.sdk.net.NetContext;
import com.android.sdk.net.errorhandler.ErrorHandler;
import com.android.sdk.net.service.ServiceFactory;
import com.app.base.data.AppErrorHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:06
 */
@Module
public class DataModule {

    private static final String DEFAULT_CACHE_ID = "gw-parent-default-cache-id";

    @Singleton
    @Provides
    ServiceFactory provideServiceFactory() {
        return NetContext.get().serviceFactory();
    }

    @Provides
    @Singleton
    ErrorHandler provideErrorHandler() {
        return new AppErrorHandler();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return NetContext.get().httpClient();
    }

    /**
     * 缓存工程
     */
    @Provides
    @Singleton
    CacheFactory provideCacheFactory() {
        return new CacheFactoryImpl();
    }

    /**
     * 全局默认缓存实现，不支持跨进程。
     */
    @Provides
    @Singleton
    CacheManager provideDefaultCacheManager(@ContextType Context context) {
        return new MMKVCacheImpl(context, DEFAULT_CACHE_ID);
    }

}
