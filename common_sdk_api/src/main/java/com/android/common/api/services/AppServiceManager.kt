package com.android.common.api.services

/** A class that manages all the app services. */
interface AppServiceManager {

    fun <T : AppService> getService(clazz: Class<T>): T?

    fun <T : AppService> requireService(clazz: Class<T>): T

}