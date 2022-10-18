package com.app.base.upgrade

import android.Manifest
import android.app.Dialog
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.android.base.utils.BaseUtils
import com.blankj.utilcode.util.AppUtils
import com.android.base.utils.android.compat.AndroidVersion
import com.android.base.utils.android.onTopActivity
import com.android.base.utils.android.views.getColorCompat
import com.android.base.utils.common.otherwise
import com.android.base.utils.common.yes
import com.android.sdk.upgrade.UpgradeException
import com.android.sdk.upgrade.UpgradeInfo
import com.android.sdk.upgrade.UpgradeInteractor
import com.app.base.R
import com.app.base.app.DispatcherProvider
import com.android.common.api.network.ApiServiceFactoryProvider
import com.app.base.config.AppPrivateDirectories
import com.app.base.config.AppSettings
import com.app.base.injection.ApplicationScope
import com.app.base.widget.dialog.showConfirmDialog
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.File
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-10-30 11:26
 */
internal class AppUpgradeInteractor @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider
) : UpgradeInteractor {

    @Inject lateinit var appSettings: AppSettings

    @Inject lateinit var apiServiceFactoryProvider: ApiServiceFactoryProvider

    private var loadingDialogReference: WeakReference<UpgradeLoadingDialog>? = null
    private var upgradeDialogReference: WeakReference<Dialog>? = null

    private val notificationHelper by lazy { NotificationHelper() }

    private val appUpdateRepository by lazy {
        AppUpdateRepository(apiServiceFactoryProvider.getDefault())
    }

    private fun newLoadingDialogIfNeed(context: Context): Dialog {
        loadingDialogReference?.get()?.dismiss()
        val dialog = UpgradeLoadingDialog(context)
        loadingDialogReference = WeakReference(dialog)
        return dialog
    }


    private fun buildUpgradeInfo(response: UpgradeResponse): UpgradeInfo {
        return UpgradeInfo(
            isForce = false,
            isNewVersion = false,
            versionName = "",
            downloadUrl = "",
            description = "",
            digitalAbstract = "",
            raw = response
        )
    }

    override fun checkUpgrade(onStart: () -> Unit, onError: (Throwable) -> Unit, onSuccess: (UpgradeInfo) -> Unit) {
        onStart()
        appUpdateRepository.checkNewVersion()
            .map {
                buildUpgradeInfo(it)
            }
            .flowOn(dispatcherProvider.io())
            .catch {
                onError(it)
            }
            .onEach {
                onSuccess(it)
            }
            .flowOn(dispatcherProvider.ui())
            .launchIn(scope)
    }

    override fun showUpgradeDialog(
        context: Context,
        upgradeInfo: UpgradeInfo,
        onCancel: () -> Unit,
        onConfirm: () -> Unit
    ) {
        Timber.d("showUpgradeDialog")
        upgradeDialogReference?.get()?.dismiss()

        val upgradeDialog = showConfirmDialog(context) {
            title = "更新提示"
            message = upgradeInfo.description
            messageColor = context.getColorCompat(R.color.opacity50_black)
            positiveText = "立即更新"
            positiveListener = { onConfirm() }
            if (!upgradeInfo.isForce) {
                negativeListener = {
                    onCancel()
                }
            } else {
                disableNegative()
            }
            cancelable = false
        }

        upgradeDialogReference = WeakReference(upgradeDialog)
    }

    override fun showInstallTipsDialog(
        context: Context,
        forceUpgrade: Boolean,
        onCancel: () -> Unit,
        onConfirm: () -> Unit
    ) {
        if (!forceUpgrade) {
            return
        }
        showConfirmDialog(context) {
            message = "新版本已经下载完成，请点击“确认”进行安装"
            cancelable = false
            negativeText = null
            negativeListener = {
                it.dismiss()
                onCancel()
            }
            positiveListener = { onConfirm() }
            autoDismiss = false
        }
    }

    override fun showDownloadingFailed(
        context: Context,
        forceUpgrade: Boolean,
        error: UpgradeException,
        onCancel: () -> Unit,
        onConfirm: () -> Unit
    ) {
        showConfirmDialog(context) {
            message = forceUpgrade.yes { "下载更新失败，需要重试" } otherwise { "下载更新失败，是否重试？" }
            cancelable = false
            negativeText = forceUpgrade.yes { null } otherwise { "取消" }
            negativeListener = { onCancel() }
            positiveListener = { onConfirm() }
        }
    }

    override fun showDownloadingDialog(context: Context, forceUpgrade: Boolean) {
        Timber.d("showDownloadingDialog")
        notificationHelper.cancelNotification()

        newLoadingDialogIfNeed(context).run {
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
        loadingDialogReference?.get()?.notifyProgress(total, progress)
    }

    override fun installApk(file: File, upgradeInfo: UpgradeInfo) {
        Timber.d("installApk")
        if (AndroidVersion.atLeast(26)) {
            //Android8.0未知来源应用安装权限方案
            onTopActivity {
                PermissionX.init(it as FragmentActivity)
                    .permissions(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                    .onExplainRequestReason { scope, deniedList ->
                        val message = "PermissionX需要您同意以下权限才能正常使用"
                        scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
                    }
                    .request { allGranted, _, _ ->
                        if (allGranted) {
                            AppUtils.installApp(BaseUtils.getAppContext(), file, appSettings.appFileProviderAuthorities)
                        }
                    }
            }
        } else {
            //正常安装
            AppUtils.installApp(BaseUtils.getAppContext(), file, appSettings.appFileProviderAuthorities)
        }
    }

    override fun checkApkFile(apkFile: File, digitalAbstract: String) = true

    override fun generateAppDownloadPath(versionName: String): String =
        AppPrivateDirectories.createAppDownloadPath(versionName)

    override fun createHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

}