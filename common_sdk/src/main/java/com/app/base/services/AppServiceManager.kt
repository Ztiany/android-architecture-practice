package com.app.base.services

interface AppServiceManager {

    fun registerService(name: String, appService: AppService)

    fun unregisterService(name: String): Boolean

    fun <T : AppService> getService(name: String): T?

    fun <T : AppService> requireService(name: String): T

}