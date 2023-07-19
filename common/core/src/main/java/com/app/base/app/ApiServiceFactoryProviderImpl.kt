package com.app.base.app

import android.util.Log
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.service.ServiceFactory
import com.android.sdk.net.extension.defaultServiceFactory
import com.app.common.api.network.ApiServiceFactoryProvider
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class ApiServiceFactoryProviderImpl @Inject constructor() : ApiServiceFactoryProvider {

    init {
        Log.d("ApiServiceFactoryProvider", "ApiServiceFactoryProviderImpl initialized.")
    }

    override fun getDefault(): ServiceFactory {
        return NetContext.get().defaultServiceFactory()
    }

    override fun get(flag: String): ServiceFactory {
        return NetContext.get().serviceFactory(flag)
    }

}