package com.android.common.api.services

typealias OnService = (service: AppService) -> Unit

/** A class that manages all the app services. */
interface AppServiceManager {

    fun registerService(name: String, appService: AppService)

    fun registerService(name: String, factory: AppServiceFactory)

    fun unregisterService(name: String): Boolean

    fun <T : AppService> getService(name: String): T?

    fun <T : AppService> requireService(name: String): T

    fun onService(name: String, action: OnService)

    fun cancelOnService(name: String, action: OnService)


}