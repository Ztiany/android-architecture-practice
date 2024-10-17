package com.app.base.component.errorhandler

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.android.sdk.net.NetContext
import com.app.base.data.protocol.isApiAuthenticationExpired
import com.app.base.data.protocol.isGlobalApiError
import com.app.base.dialog.toast.ToastKit
import com.app.base.ui.dialog.alertDialog
import com.app.base.ui.dialog.dsl.alert.AlertDialogInterface
import com.app.base.ui.dialog.dsl.noCancelable
import com.app.base.ui.dialog.dsl.onDismiss
import com.app.common.api.errorhandler.ErrorHandler
import com.app.common.api.protocol.CannotShowDialogOnIt
import com.app.common.api.protocol.CannotShowExpiredDialogOnIt
import com.app.common.api.router.AppRouter
import com.app.common.api.usermanager.UserManager
import com.blankj.utilcode.util.ActivityUtils
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import me.ztiany.wan.account.api.AccountModuleNavigator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AppErrorHandler @Inject constructor(
    private val appRouter: AppRouter,
    private val userManager: Lazy<UserManager>,
    @ApplicationContext private val context: Context,
) : ErrorHandler {

    private var alertDialogInterface: AlertDialogInterface? = null

    private val handler = Handler(Looper.getMainLooper())

    override fun generateMessage(throwable: Throwable): CharSequence {
        return NetContext.get().errorMessageFactory.createMessage(throwable)
    }

    override fun handleError(throwable: Throwable) {
        handler.post {
            if (!throwable.isGlobalApiError()) {
                ToastKit.showMessage(context, generateMessage(throwable))
            } else {
                handleGlobalError(throwable)
            }
        }
    }

    override fun handleGlobalError(throwable: Throwable) {
        if (!throwable.isGlobalApiError()) {
            return
        }
        handler.post {
            (ActivityUtils.getTopActivity() as? ComponentActivity)?.run {
                val message = throwable.takeIf {
                    it.isApiAuthenticationExpired()
                }?.message.takeIf {
                    !it.isNullOrEmpty()
                } ?: getString(com.app.base.ui.theme.R.string.error_service_error)
                showUserStateLostDialog(message)
            }
        }
    }

    private fun ComponentActivity.showUserStateLostDialog(errorMessage: String) {
        if (this is CannotShowDialogOnIt) {
            return
        }

        if (this is CannotShowExpiredDialogOnIt) {
            // Cannot show dialog on it, then maybe show a toast is useful for user to know the error.
            ToastKit.showMessage(context, errorMessage)
            return
        }

        if (alertDialogInterface != null) {
            return
        }

        alertDialog {
            message(errorMessage)
            positiveButton(com.app.base.ui.theme.R.string.confirm) {
                alertDialogInterface = null
                userManager.get().logout()
                appRouter.getNavigator(AccountModuleNavigator::class.java)?.openLoginPage(
                    context = this@showUserStateLostDialog,
                    clearStack = true
                )
            }
            noCancelable()
            onDismiss {
                alertDialogInterface = null
            }
        }.apply {
            alertDialogInterface = show()
        }
    }

}