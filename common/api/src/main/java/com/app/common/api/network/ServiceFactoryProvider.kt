package com.app.common.api.network

import com.android.sdk.net.core.service.ServiceFactory

interface ServiceFactoryProvider {

    fun getDefault(): ServiceFactory

    fun get(flag: String): ServiceFactory

}