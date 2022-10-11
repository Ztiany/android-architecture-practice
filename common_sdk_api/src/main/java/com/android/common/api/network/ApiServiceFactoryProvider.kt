package com.android.common.api.network

import com.android.sdk.net.core.service.ServiceFactory

interface ApiServiceFactoryProvider {

    fun getDefault(): ServiceFactory

    fun get(flag: String): ServiceFactory

}