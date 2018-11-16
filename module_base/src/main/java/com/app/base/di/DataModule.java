package com.app.base.di;

import android.content.Context;

import com.android.base.app.dagger.ContextType;
import com.android.sdk.cache.MMKVStorageFactoryImpl;
import com.android.sdk.cache.MMKVStorageImpl;
import com.android.sdk.cache.Storage;
import com.android.sdk.cache.StorageFactory;
import com.android.sdk.net.NetContext;
import com.android.sdk.net.errorhandler.ErrorHandler;
import com.android.sdk.net.service.ServiceFactory;
import com.app.base.widget.dialog.TipsManager;

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
        return new ErrorHandler() {

            @Override
            public CharSequence createMessage(Throwable throwable) {
                return ErrorHandler.createDefaultErrorMessage(throwable);
            }

            @Override
            public void handleError(Throwable throwable) {
                TipsManager.showMessage(createMessage(throwable));
            }
        };
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
    StorageFactory provideCacheFactory() {
        return new MMKVStorageFactoryImpl();
    }

    /**
     * 全局默认缓存实现，不支持跨进程。
     */
    @Provides
    @Singleton
    Storage provideDefaultCacheManager(@ContextType Context context) {
        return new MMKVStorageImpl(context, DEFAULT_CACHE_ID);
    }

}
