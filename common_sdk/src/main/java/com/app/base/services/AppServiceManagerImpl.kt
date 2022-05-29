package com.app.base.services

internal class AppServiceManagerImpl : AppServiceManager {

    override fun registerService(name: String, appService: AppService) {
        TODO("Not yet implemented")
    }

    override fun unregisterService(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun <T : AppService> getService(name: String): T? {
        TODO("Not yet implemented")
    }

    override fun <T : AppService> requireService(name: String): T {
        TODO("Not yet implemented")
    }

}