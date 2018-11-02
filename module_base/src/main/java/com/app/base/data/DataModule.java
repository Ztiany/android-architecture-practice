package com.app.base.data;

import android.content.Context;

import com.android.sdk.cache.CacheManager;
import com.android.sdk.cache.MMKVCacheImpl;
import com.app.base.BuildConfig;
import com.app.base.data.cache.CacheFactory;
import com.app.base.data.cache.CacheFactoryImpl;
import com.app.base.data.gson.GsonUtils;
import com.app.base.di.qualifier.ContextType;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:06
 */
@Module
public class DataModule {

    private static final int CONNECTION_TIME_OUT = 10;
    private static final int IO_TIME_OUT = 20;

    private static final String DEFAULT_CACHE_ID = "gw-parent-default-cache-id";

    @Singleton
    @Provides
    ServiceFactory provideServiceFactory(OkHttpClient okHttpClient, URLProvider urlProvider) {
        return new ServiceFactory(okHttpClient, GsonUtils.gson(), urlProvider);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(@ContextType Context context) {

        //build OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(IO_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(IO_TIME_OUT, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> {
                    Timber.d("hostnameVerifier called with: hostname 、session = [" + hostname + "、" + session.getProtocol() + "]");
                    return true;
                });

        //debug
        if (BuildConfig.openDebug) {
            HttpClientDebugConfig.config(builder, context);
        }

        return builder.build();
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
