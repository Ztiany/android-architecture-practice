package com.app.base.app

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.base.AndroidSword
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.app.base.data.protocol.ApiHelper
import com.app.base.router.AppRouter
import com.app.base.component.usermanager.UserManager
import com.app.base.component.usermanager.isUserLogin
import com.app.base.widget.dialog.TipsTool
import com.app.base.widget.dialog.showConfirmDialog
import com.vclusters.cloud.main.api.MainModule
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:02
 */
interface ErrorHandler {

    /** 根据异常，生成一个合理的错误提示 */
    fun generateMessage(throwable: Throwable): CharSequence

    /** 直接处理异常，比如根据 [generateMessage] 方法生成的消息弹出一个 toast。 */
    fun handleError(throwable: Throwable)

    /**处理全局异常，此方法仅由数据层调用，用于统一处理全局异常*/
    fun handleGlobalError(throwable: Throwable)

}

@Singleton
internal class AppErrorHandler @Inject constructor(
    private val appRouter: AppRouter,
    private val userManager: UserManager,
    @ApplicationContext private val context: Context
) : ErrorHandler {

    private var showingDialog: WeakReference<Dialog>? = null

    private val handler = Handler(Looper.getMainLooper())

    override fun generateMessage(throwable: Throwable): CharSequence {
        return NetContext.get().createMessage(throwable)
    }

    override fun handleError(throwable: Throwable) {
        if (!(throwable is ApiErrorException && ApiHelper.isLoginExpired(throwable.code))) {
            TipsTool.showMessage(context, generateMessage(throwable))
        }
    }

    override fun handleGlobalError(throwable: Throwable) {
        handler.post {
            if (throwable is ApiErrorException && isTokenExpired(throwable) && userManager.isUserLogin()) {
                showReLoginDialog()
            }
        }
    }

    private fun isTokenExpired(throwable: ApiErrorException): Boolean {
        return ApiHelper.isAuthenticationExpired(throwable)
    }

    private fun showReLoginDialog(): Boolean {
        val currentActivity = AndroidSword.topActivity ?: return false

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

        val showConfirmDialog = currentActivity.showConfirmDialog {
            message = "您的登录已过期，请重新登录"
            disableNegative()
            positiveListener = {
                showingDialog = null
                //handle login expired
                appRouter.build(MainModule.PATH)
                    .withInt(MainModule.ACTION_KEY, MainModule.ACTION_RE_LOGIN)
                    .navigation()
            }
        }

        showConfirmDialog.setCancelable(false)
        showConfirmDialog.setOnDismissListener {
            showingDialog = null
        }

        showingDialog = WeakReference(showConfirmDialog)

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

}