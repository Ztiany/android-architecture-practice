package com.app.base.di

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.service.ServiceFactory
import com.app.base.app.AppErrorHandler
import com.app.base.app.ErrorHandler
import com.app.base.data.app.AppDataSource
import com.app.base.data.app.AppRepository
import com.app.base.data.app.StorageManager
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:06
 */
@Module
class DataModule {

    @Provides
    internal fun provideUserDataSource(appRepository: AppRepository): AppDataSource {
        return appRepository
    }

    @Singleton
    @Provides
    internal fun provideServiceFactory(): ServiceFactory {
        return NetContext.get().serviceFactory()
    }

    @Provides
    @Singleton
    internal fun provideErrorHandler(): ErrorHandler {
        return AppErrorHandler()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        return NetContext.get().httpClient()
    }

}
