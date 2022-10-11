package com.app.base.component.services

import com.android.common.api.services.AppService
import com.android.common.api.services.AppServiceManager
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class AppServiceManagerImpl @Inject constructor(
    private val appServiceMap: Map<Class<out AppService>, @JvmSuppressWildcards Provider<AppService>>
) : AppServiceManager {

    @Suppress("UNCHECKED_CAST")
    override fun <T : AppService> getService(clazz: Class<T>): T? {
        return appServiceMap[clazz]?.get() as? T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : AppService> requireService(clazz: Class<T>): T {
        return appServiceMap[clazz]?.get() as? T ?: throw NullPointerException("The service you required is not provided.")
    }

}