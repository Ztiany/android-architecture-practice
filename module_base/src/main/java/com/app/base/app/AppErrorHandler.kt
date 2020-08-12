package com.app.base.app

import com.android.sdk.net.core.errorhandler.ErrorMessageFactory
import com.android.sdk.net.core.exception.ApiErrorException
import com.app.base.data.api.ApiHelper
import com.app.base.widget.dialog.TipsManager

internal class AppErrorHandler : ErrorHandler {

    override fun generateMessage(throwable: Throwable): CharSequence {
        return ErrorMessageFactory.createMessage(throwable)
    }

    override fun handleError(throwable: Throwable) {
        if (!(throwable is ApiErrorException && ApiHelper.isLoginExpired(throwable.code))) {
            TipsManager.showMessage(generateMessage(throwable))
        }
    }

    override fun handleGlobalError(throwable: Throwable) {
    }

}