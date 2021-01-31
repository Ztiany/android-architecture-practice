package com.app.base.di

import android.content.Context
import com.android.base.concurrent.DispatcherProvider
import com.android.base.concurrent.SchedulerProvider
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.service.ServiceFactory
import com.app.base.app.AppErrorHandler
import com.app.base.app.ErrorHandler
import com.app.base.data.app.*
import com.app.base.data.app.AppRepository
import com.app.base.services.ServiceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:06
 */
@Module
@InstallIn(ApplicationComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideUserDataSource(
            @ApplicationContext context: Context,
            serviceFactory: ServiceFactory,
            schedulerProvider: SchedulerProvider,
            storageManager: StorageManager
    ): AppDataSource {
        return AppRepository(context,serviceFactory, storageManager)
    }

    @Singleton
    @Provides
    fun provideServiceFactory(): ServiceFactory {
        return NetContext.get().serviceFactory()
    }

    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler {
        return AppErrorHandler()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return NetContext.get().httpClient()
    }

    @Provides
    @Singleton
    fun provideServiceManager(
            serviceFactory: ServiceFactory,
            schedulerProvider: SchedulerProvider,
            dispatcherProvider: DispatcherProvider
    ): ServiceManager {
        return ServiceManager(serviceFactory, schedulerProvider, dispatcherProvider)
    }

}