package com.app.base.app

import com.android.common.api.network.ApiServiceFactoryProvider
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.service.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class ApiServiceFactoryProviderImpl @Inject constructor() : ApiServiceFactoryProvider {

    override fun getDefault(): ServiceFactory {
        return NetContext.get().serviceFactory()
    }

    override fun get(flag: String): ServiceFactory {
        return NetContext.get().serviceFactory(flag)
    }

}