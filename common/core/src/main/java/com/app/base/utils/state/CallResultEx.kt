package com.app.base.utils.state

import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.onError
import com.app.common.api.errorhandler.ErrorHandler

fun <T> CallResult<T>.noticeOnError(errorHandler: ErrorHandler) {
    onError {
        errorHandler.handleError(it)
    }
}