package com.app.base.app

import android.app.Dialog
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.base.app.Sword
import com.android.sdk.net.core.errorhandler.ErrorMessageFactory
import com.android.sdk.net.core.exception.ApiErrorException
import com.app.base.data.protocol.ApiHelper
import com.app.base.router.AppRouter
import com.app.base.router.RouterPath
import com.app.base.services.usermanager.AppDataSource
import com.app.base.widget.dialog.TipsManager
import com.app.base.widget.dialog.showConfirmDialog
import java.lang.ref.WeakReference

internal class AppErrorHandler(
    private val appRouter: AppRouter,
    private val appDataSource: AppDataSource
) : ErrorHandler {

    private var showingDialog: WeakReference<Dialog>? = null

    private val handler = Handler(Looper.getMainLooper())

    override fun generateMessage(throwable: Throwable): CharSequence {
        return ErrorMessageFactory.createMessage(throwable)
    }

    override fun handleError(throwable: Throwable) {
        if (!(throwable is ApiErrorException && ApiHelper.isLoginExpired(throwable.code))) {
            TipsManager.showMessage(generateMessage(throwable))
        }
    }

    override fun handleGlobalError(throwable: Throwable) {
        handler.post {
            if (throwable is ApiErrorException && isTokenExpired(throwable) && appDataSource.userLogined()) {
                showReLoginDialog()
            }
        }
    }

    private fun isTokenExpired(throwable: ApiErrorException): Boolean {
        return ApiHelper.isAuthenticationExpired(throwable)
    }

    private fun showReLoginDialog(): Boolean {
        val currentActivity = Sword.topActivity ?: return false

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
                appRouter.build(RouterPath.Main.PATH)
                    .withInt(RouterPath.Main.ACTION_KEY, RouterPath.Main.ACTION_RE_LOGIN)
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