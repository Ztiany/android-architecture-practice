package com.app.base.upgrade

import android.content.Context
import com.android.sdk.upgrade.UpgradeException
import com.android.sdk.upgrade.UpgradeInfo
import com.android.sdk.upgrade.UpgradeInteractor
import com.app.base.app.Platform
import com.app.base.config.AppSettings
import com.app.base.injection.ApplicationScope
import com.app.common.api.dispatcher.DispatcherProvider
import com.app.common.api.network.ServiceFactoryProvider
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@author Ztiany
 */
@Singleton
internal class AppUpgradeInteractor @Inject constructor() : UpgradeInteractor {

    @Inject @ApplicationScope lateinit var scope: CoroutineScope

    @Inject lateinit var dispatcherProvider: DispatcherProvider

    @Inject lateinit var platform: Platform

    @Inject lateinit var appSettings: AppSettings

    @Inject lateinit var apiServiceFactoryProvider: ServiceFactoryProvider

    override fun checkUpgrade(onStart: () -> Unit, onError: (Throwable) -> Unit, onSuccess: (UpgradeInfo) -> Unit) {

    }

    override fun showUpgradeDialog(context: Context, upgradeInfo: UpgradeInfo, onCancel: () -> Unit, onConfirm: () -> Unit) {

    }

    override fun showInstallTipsDialog(context: Context, forceUpgrade: Boolean, onCancel: () -> Unit, onConfirm: () -> Unit) {

    }

    override fun showDownloadingFailed(
        context: Context,
        forceUpgrade: Boolean,
        error: UpgradeException,
        onCancel: () -> Unit,
        onConfirm: () -> Unit,
    ) {

    }

    override fun showDownloadingDialog(context: Context, forceUpgrade: Boolean) {

    }

    override fun dismissDownloadingDialog() {

    }

    override fun onProgress(total: Long, progress: Long) {

    }

    override fun installApk(file: File, upgradeInfo: UpgradeInfo) {

    }

    override fun checkApkFile(apkFile: File, digitalAbstract: String): Boolean {
        return false
    }

    override fun generateAppDownloadPath(versionName: String): String {
        return ""
    }

    override fun createHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    override fun upPrepared() {

    }

}