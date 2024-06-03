package com.app.base.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.app.base.data.protocol.ApiHelper
import com.app.base.widget.dialog.confirm.ConfirmDialogInterface
import com.app.base.widget.dialog.ToastKit
import com.app.base.widget.dialog.base.onDismiss
import com.app.base.widget.dialog.confirm.showConfirmDialog
import com.app.common.api.router.AppRouter
import com.app.common.api.usermanager.UserManager
import com.app.common.api.usermanager.isUserLogin
import me.ztiany.wan.main.MainModuleNavigator
import com.blankj.utilcode.util.ActivityUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Ztiany
 */
interface ErrorHandler {

    /** 根据异常，生成一个合理的错误提示。 */
    fun generateMessage(throwable: Throwable): CharSequence

    /** 直接处理异常，比如根据 [generateMessage] 方法生成的消息弹出一个 toast。 */
    fun handleError(throwable: Throwable)

    /**处理全局异常，此方法仅由数据层调用，用于统一处理全局异常。 */
    fun handleGlobalError(throwable: Throwable)

}

@Singleton
internal class AppErrorHandler @Inject constructor(
    private val appRouter: AppRouter,
    private val userManager: UserManager,
    @ApplicationContext private val context: Context
) : ErrorHandler {

    init {
        Log.d("ErrorHandler", "AppErrorHandler initialized.")
    }

    private var showingDialog: WeakReference<ConfirmDialogInterface>? = null

    private val handler = Handler(Looper.getMainLooper())

    override fun generateMessage(throwable: Throwable): CharSequence {
        return NetContext.get().errorMessageFactory.createMessage(throwable)
    }

    override fun handleError(throwable: Throwable) {
        if (!isTokenExpired(throwable)) {
            ToastKit.showMessage(context, generateMessage(throwable))
        }
    }

    override fun handleGlobalError(throwable: Throwable) {
        handler.post {
            if (isTokenExpired(throwable) && userManager.isUserLogin()) {
                showReLoginDialog(throwable as ApiErrorException)
            }
        }
    }

    private fun isTokenExpired(throwable: Throwable): Boolean {
        return ApiHelper.isAuthenticationExpired(throwable)
    }

    private fun showReLoginDialog(apiErrorException: ApiErrorException): Boolean {
        val currentActivity = ActivityUtils.getTopActivity() ?: return false

        if (currentActivity is CannotShowDialogOnIt) {
            return false
        }

        if (currentActivity is CannotShowExpiredDialogOnIt) {
            return false
        }

        val dialog = showingDialog?.get()

        if (dialog != null) {
            return true
        }

        val confirmDialog = currentActivity.showConfirmDialog {
            message = createMessageByErrorType(apiErrorException)
            disableNegative()
            cancelable = false
            positiveListener = {
                showingDialog = null
                //handle login expired
                ActivityUtils.getTopActivity()?.let {
                    appRouter.getNavigator(MainModuleNavigator::class.java)?.exitAndLogin(it)
                }
            }
        }.also {
            it.onDismiss {
                showingDialog = null
            }
        }

        showingDialog = WeakReference(confirmDialog)

        /*activity maybe finish by programming*/
        if (currentActivity is LifecycleOwner) {
            currentActivity.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    showingDialog?.get()?.dismiss()
                    showingDialog = null
                }
            })
        }
        return true
    }

    private fun createMessageByErrorType(apiErrorException: ApiErrorException): CharSequence {
        return when (apiErrorException.code) {
            ApiHelper.SSO -> "您的账号已在其他设备登录"
            ApiHelper.TOKEN_EXPIRED, ApiHelper.TOKEN_INVALIDATE -> "您的登录已过期，请重新登录"
            else -> "您的登录已过期，请重新登录"
        }
    }

}