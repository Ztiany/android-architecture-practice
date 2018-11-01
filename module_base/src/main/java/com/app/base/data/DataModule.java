package com.app.base.data;

import android.content.Context;

import com.app.base.BuildConfig;
import com.app.base.data.cache.CacheManager;
import com.app.base.data.cache.MMKVCache;
import com.app.base.data.net.AutoGenTypeAdapterFactory;
import com.app.base.di.qualifier.ContextType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    @Singleton
    @Provides
    ServiceFactory provideServiceFactory(OkHttpClient okHttpClient, URLProvider urlProvider) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new AutoGenTypeAdapterFactory())
                .create();
        return new ServiceFactory(okHttpClient, gson, urlProvider);
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

    @Provides
    @Singleton
    CacheManager provideCacheManager(MMKVCache mmkvCache) {
        return mmkvCache;
    }


}
