package com.android.sdk.upgrade.impl

import com.android.sdk.net.coroutines.executeApiCall
import com.app.base.app.Platform
import com.app.common.api.dispatcher.DispatcherProvider
import kotlinx.coroutines.withContext

internal class AppUpgradeRepository(
    private val appUpgradeApi: AppUpgradeApi,
    private val dispatcherProvider: DispatcherProvider,
    private val platform: Platform
) {

    suspend fun checkNewVersion(): UpgradeResponse {
        return withContext(dispatcherProvider.io()) {
            executeApiCall {
                appUpgradeApi.loadNewVersionInfo(
                    platform.getPackageName(),
                    platform.getAppVersionName()
                )
            }
        }
    }

}