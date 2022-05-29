package com.app.base.app

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.service.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

interface ServiceProvider {

    fun getDefault(): ServiceFactory

    fun get(flag: String): ServiceFactory

}

@Singleton
internal class ServiceProviderImpl @Inject constructor() : ServiceProvider {

    override fun getDefault(): ServiceFactory {
        return NetContext.get().serviceFactory()
    }

    override fun get(flag: String): ServiceFactory {
        return NetContext.get().serviceFactory(flag)
    }

}