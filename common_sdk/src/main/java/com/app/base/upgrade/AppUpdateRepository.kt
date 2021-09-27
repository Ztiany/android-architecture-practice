package com.app.base.upgrade

import com.android.sdk.net.core.service.ServiceFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal data class UpgradeResponse(
    val url: String = "",
    val forceUpdate: Boolean = false,
    val isUpdate: Boolean = false,
    val md5: String = "",
    val size: String = "",
    val updateLog: String = "",
    val ver: String = ""
)

internal class AppUpdateRepository(serviceFactory: ServiceFactory) {

    fun checkNewVersion(): Flow<UpgradeResponse> = flow {

    }

}