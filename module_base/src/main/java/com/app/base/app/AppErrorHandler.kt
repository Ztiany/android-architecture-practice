package com.app.base.app

import android.app.Dialog
import com.android.sdk.net.errorhandler.ErrorMessageFactory
import java.lang.ref.WeakReference

internal class AppErrorHandler : ErrorHandler {

    private var showingDialog: WeakReference<Dialog>? = null

    override fun generateMessage(throwable: Throwable): CharSequence {
        return ErrorMessageFactory.createMessage(throwable)
    }

    override fun handleError(throwable: Throwable) {
    }

    override fun handleGlobalError(throwable: Throwable) {
    }

    private fun showReLoginDialog(code: Int): Boolean {
        return true
    }

}