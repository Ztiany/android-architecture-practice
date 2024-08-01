package com.app.base.component.errorhandler

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.app.common.api.protocol.CannotShowDialogOnIt
import com.app.common.api.protocol.CannotShowExpiredDialogOnIt
import com.app.base.data.protocol.isGlobalApiError
import com.app.base.widget.dialog.base.onDismiss
import com.app.base.widget.dialog.confirm.ConfirmDialogInterface
import com.app.base.widget.dialog.confirm.showConfirmDialog
import com.app.base.widget.dialog.toast.ToastKit
import com.app.common.api.errorhandler.ErrorHandler
import com.app.common.api.router.AppRouter
import com.app.common.api.usermanager.UserManager
import com.app.common.api.usermanager.isUserLogin
import com.blankj.utilcode.util.ActivityUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import me.ztiany.wan.main.MainModuleNavigator
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AppErrorHandler @Inject constructor(
    private val appRouter: AppRouter,
    private val userManager: UserManager,
    @ApplicationContext private val context: Context,
) : ErrorHandler {

    private var showingDialog: WeakReference<ConfirmDialogInterface>? = null

    private val handler = Handler(Looper.getMainLooper())

    override fun generateMessage(throwable: Throwable): CharSequence {
        return NetContext.get().errorMessageFactory.createMessage(throwable)
    }

    override fun handleError(throwable: Throwable) {
        if (!throwable.isGlobalApiError()) {
            ToastKit.showMessage(context, generateMessage(throwable))
        }
    }

    override fun handleGlobalError(throwable: Throwable) {
        handler.post {
            if (throwable.isGlobalApiError() && userManager.isUserLogin()) {
                showReLoginDialog(throwable)
            }
        }
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
        return "您的登录已过期，请重新登录"
    }

}