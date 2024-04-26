package com.android.sdk.upgrade.impl

import android.Manifest
import android.app.Dialog
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.android.base.utils.BaseUtils
import com.android.base.utils.android.InstallUtils
import com.android.base.utils.android.compat.AndroidVersion
import com.android.base.utils.common.otherwise
import com.android.base.utils.common.yes
import com.android.sdk.net.extension.create
import com.android.sdk.upgrade.UpgradeException
import com.android.sdk.upgrade.UpgradeInfo
import com.android.sdk.upgrade.UpgradeInteractor
import com.app.base.R
import com.app.base.app.AndroidPlatform
import com.app.base.app.DispatcherProvider
import com.app.base.config.AppPrivateDirectories
import com.app.base.config.AppSettings
import com.app.base.injection.ApplicationScope
import com.app.base.widget.dialog.showConfirmDialog
import com.app.common.api.network.ApiServiceFactoryProvider
import com.blankj.utilcode.util.ActivityUtils
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.File
import java.lang.ref.WeakReference
import javax.inject.Inject
import com.app.base.ui.R as UI_R


/**
 *@author Ztiany
 */
internal class AppUpgradeInteractor @Inject constructor() : UpgradeInteractor {

    @Inject @ApplicationScope lateinit var scope: CoroutineScope

    @Inject lateinit var dispatcherProvider: DispatcherProvider

    @Inject lateinit var androidPlatform: AndroidPlatform

    @Inject lateinit var appSettings: AppSettings

    @Inject lateinit var apiServiceFactoryProvider: ApiServiceFactoryProvider

    private var loadingDialogReference: WeakReference<UpgradeLoadingDialog>? = null
    private var upgradeDialogReference: WeakReference<Dialog>? = null

    private val notificationHelper by lazy { NotificationHelper() }

    private val appUpgradeRepository by lazy {
        AppUpgradeRepository(
            apiServiceFactoryProvider.getDefault().create(),
            dispatcherProvider,
            androidPlatform
        )
    }

    private fun newLoadingDialogIfNeed(context: Context, forceUpgrade: Boolean): Dialog {
        loadingDialogReference?.get()?.dismiss()
        val dialog = UpgradeLoadingDialog(context, forceUpgrade)
        loadingDialogReference = WeakReference(dialog)
        return dialog
    }

    private fun buildUpgradeInfo(response: UpgradeResponse): UpgradeInfo {
        return UpgradeInfo(
            isForce = response.isForce,
            isNewVersion = response.packageName == androidPlatform.getPackageName() && response.versionNumber > androidPlatform.getAppVersionName(),
            versionName = response.versionNumber,
            downloadUrl = response.url,
            description = response.versionDescription,
            digitalAbstract = "",
            raw = response
        )
    }

    override fun checkUpgrade(onStart: () -> Unit, onError: (Throwable) -> Unit, onSuccess: (UpgradeInfo) -> Unit) {
        scope.launch {
            onStart()
            try {
                onSuccess(buildUpgradeInfo(appUpgradeRepository.checkNewVersion()))
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    override fun showUpgradeDialog(
        context: Context,
        upgradeInfo: UpgradeInfo,
        onCancel: () -> Unit,
        onConfirm: () -> Unit,
    ) {
        Timber.d("showUpgradeDialog")
        upgradeDialogReference?.get()?.dismiss()

        val upgradeDialog = UpgradeDialog(context, upgradeInfo.isForce, upgradeInfo.versionName, upgradeInfo.description).apply {
            positiveListener = { onConfirm() }
            negativeListener = { onCancel() }
        }

        upgradeDialog.show()

        upgradeDialogReference = WeakReference(upgradeDialog)
    }

    override fun showInstallTipsDialog(
        context: Context,
        forceUpgrade: Boolean,
        onCancel: () -> Unit,
        onConfirm: () -> Unit,
    ) {
        if (!forceUpgrade) {
            return
        }
        showConfirmDialog(context) {
            messageId = R.string.upgrade_do_install
            cancelable = false
            negativeText = null
            negativeListener = {
                it.dismiss()
                onCancel()
            }
            positiveId = UI_R.string.confirm
            positiveListener = { onConfirm() }
            autoDismiss = false
        }
    }

    override fun showDownloadingFailed(
        context: Context, forceUpgrade: Boolean, error: UpgradeException, onCancel: () -> Unit, onConfirm: () -> Unit,
    ) {
        showConfirmDialog(context) {
            message = forceUpgrade.yes {
                context.getString(R.string.upgrade_download_failed_force_retry)
            } otherwise {
                context.getString(R.string.upgrade_download_failed_retry)
            }
            cancelable = false
            negativeText = forceUpgrade.yes { null } otherwise { context.getString(UI_R.string.cancel) }
            negativeListener = { onCancel() }
            positiveListener = { onConfirm() }
        }
    }

    override fun showDownloadingDialog(context: Context, forceUpgrade: Boolean) {
        Timber.d("showDownloadingDialog")
        notificationHelper.cancelNotification()

        newLoadingDialogIfNeed(context, forceUpgrade).run {
            setCancelable(false)
            show()
        }
    }

    override fun dismissDownloadingDialog() {
        Timber.d("showDownloadingDialog")
        notificationHelper.cancelNotification()
        loadingDialogReference?.get()?.dismiss()
    }

    override fun onProgress(total: Long, progress: Long) {
        Timber.d("onProgress, total = $total, progress = $progress")
        notificationHelper.notifyProgress(total, progress)
        val loadingDialog = loadingDialogReference?.get()
        if (loadingDialog != null && loadingDialog.isShowing) {
            loadingDialog.notifyProgress(total, progress)
        }
    }

    override fun installApk(file: File, upgradeInfo: UpgradeInfo) {
        Timber.d("installApk")
        if (AndroidVersion.atLeast(26)) {
            //Android8.0未知来源应用安装权限方案
            ActivityUtils.getTopActivity()?.let {
                PermissionX.init(it as FragmentActivity).permissions(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                    .onExplainRequestReason { scope, deniedList ->
                        val message = it.getString(R.string.upgrade_permission)
                        scope.showRequestReasonDialog(deniedList, message, it.getString(UI_R.string.allow), it.getString(UI_R.string.deny))
                    }.request { allGranted, _, _ ->
                        Timber.d("installApk allGranted = $allGranted")
                        if (allGranted) {
                            InstallUtils.installApp(BaseUtils.getAppContext(), file, appSettings.appFileProviderAuthorities)
                        }
                    }
            }
        } else {
            //正常安装
            InstallUtils.installApp(BaseUtils.getAppContext(), file, appSettings.appFileProviderAuthorities)
        }
    }

    override fun checkApkFile(apkFile: File, digitalAbstract: String) = true

    override fun generateAppDownloadPath(versionName: String): String = AppPrivateDirectories.createAppDownloadPath(versionName)

    override fun createHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

}