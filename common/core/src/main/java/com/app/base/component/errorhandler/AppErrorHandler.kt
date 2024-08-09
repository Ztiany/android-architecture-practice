package com.app.base.component.errorhandler

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.app.base.data.protocol.isGlobalApiError
import com.app.base.ui.dialog.alertDialog
import com.app.base.ui.dialog.dsl.alert.AlertDialogInterface
import com.app.base.dialog.toast.ToastKit
import com.app.common.api.errorhandler.ErrorHandler
import com.app.common.api.protocol.CannotShowDialogOnIt
import com.app.common.api.protocol.CannotShowExpiredDialogOnIt
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

    private var expiredAlertDialog: WeakReference<AlertDialogInterface>? = null

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

    private fun showReLoginDialog(apiErrorException: ApiErrorException) {
        val currentActivity = ActivityUtils.getTopActivity() as? ComponentActivity ?: return

        if (currentActivity is CannotShowDialogOnIt) {
            return
        }

        if (currentActivity is CannotShowExpiredDialogOnIt) {
            return
        }

        expiredAlertDialog?.get()?.dismiss()

        val confirmDialog = currentActivity.alertDialog {
            message(createMessageByErrorType(apiErrorException))
            positiveButton("确认") {
                expiredAlertDialog = null
                //handle login expired
                ActivityUtils.getTopActivity()?.let {
                    appRouter.getNavigator(MainModuleNavigator::class.java)?.exitAndLogin(it)
                }
            }
            behavior {
                cancelable(false)
                onDismiss {
                    expiredAlertDialog = null
                }
            }
        }.show()

        expiredAlertDialog = WeakReference(confirmDialog)
    }

    private fun createMessageByErrorType(apiErrorException: ApiErrorException): CharSequence {
        return "您的登录已过期，请重新登录"
    }

}