package com.app.base.di

import android.content.Context
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.service.ServiceFactory
import com.app.base.app.AppErrorHandler
import com.app.base.app.ErrorHandler
import com.app.base.data.storage.StorageManager
import com.app.base.router.AppRouter
import com.app.base.services.usermanager.AppDataSource
import com.app.base.services.usermanager.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:06
 */
@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideUserDataSource(
        @ApplicationContext context: Context,
        storageManager: StorageManager
    ): AppDataSource {
        return AppRepository(context, storageManager)
    }

    @Singleton
    @Provides
    fun provideServiceFactory(): ServiceFactory {
        return NetContext.get().serviceFactory()
    }

    @Provides
    @Singleton
    fun provideErrorHandler(appRouter: AppRouter, appDataSource: AppDataSource): ErrorHandler {
        return AppErrorHandler(appRouter, appDataSource)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return NetContext.get().httpClient()
    }

}