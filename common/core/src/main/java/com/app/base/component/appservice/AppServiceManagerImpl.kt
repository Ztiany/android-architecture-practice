package com.app.base.component.appservice

import android.util.Log
import com.app.common.api.appservice.AppService
import com.app.common.api.appservice.AppServiceManager
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class AppServiceManagerImpl @Inject constructor(
    private val appServiceMap: Map<Class<out AppService>, @JvmSuppressWildcards Provider<AppService>>
) : AppServiceManager {

    init {
        Log.d("AppServiceManager", "AppServiceManagerImpl initialized.")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : AppService> getService(clazz: Class<T>): T? {
        return appServiceMap[clazz]?.get() as? T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : AppService> requireService(clazz: Class<T>): T {
        return appServiceMap[clazz]?.get() as? T ?: throw NullPointerException("The service you required is not provided.")
    }

    override fun <T : AppService> hasService(clazz: Class<T>): Boolean {
        return appServiceMap[clazz]?.get() != null
    }

}