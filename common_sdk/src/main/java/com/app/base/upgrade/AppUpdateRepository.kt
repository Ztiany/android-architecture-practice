package com.app.base.upgrade

import com.android.sdk.net.core.service.ServiceFactory
import io.reactivex.Flowable

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

    fun checkNewVersion(): Flowable<UpgradeResponse> {
        return Flowable.empty()
    }

}