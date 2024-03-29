package com.android.sdk.upgrade.impl

import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.app.base.app.AndroidPlatform
import com.app.base.app.DispatcherProvider
import kotlinx.coroutines.withContext

internal class AppUpgradeRepository(
    private val appUpgradeApi: AppUpgradeApi,
    private val dispatcherProvider: DispatcherProvider,
    private val androidPlatform: AndroidPlatform
) {

    suspend fun checkNewVersion(): UpgradeResponse {
        return withContext(dispatcherProvider.io()) {
            executeApiCall {
                appUpgradeApi.loadNewVersionInfo(
                    androidPlatform.getPackageName(),
                    androidPlatform.getAppVersionName()
                )
            }
        }
    }

}