package com.app.base.utils

import androidx.lifecycle.MutableLiveData
import com.android.base.foundation.state.StateD
import com.android.base.foundation.state.setData
import com.android.base.foundation.state.setError
import com.android.base.foundation.state.setLoading
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.onError
import com.android.sdk.net.coroutines.onSuccess
import com.app.common.api.errorhandler.ErrorHandler
import kotlinx.coroutines.flow.MutableStateFlow

fun <T> CallResult<T>.noticeOnError(errorHandler: ErrorHandler) {
    onError {
        errorHandler.handleError(it)
    }
}

fun <T> CallResult<T>.intoState(state: MutableStateFlow<StateD<T>>) {
    onSuccess {
        state.setData(it)
    }
    onError {
        state.setError(it)
    }
}

fun <T> CallResult<T>.intoLiveData(state: MutableLiveData<StateD<T>>) {
    onSuccess {
        state.setData(it)
    }
    onError {
        state.setError(it)
    }
}

suspend fun <T> MutableLiveData<StateD<T>>.syncApiCallState(startWithLoading: Boolean = true, call: suspend () -> CallResult<T>) {
    if (startWithLoading) {
        setLoading()
    }
    call.invoke().intoLiveData(this)
}

suspend fun <T> MutableStateFlow<StateD<T>>.syncApiCallState(startWithLoading: Boolean = true, call: suspend () -> CallResult<T>) {
    if (startWithLoading) {
        setLoading()
    }
    call.invoke().intoState(this)
}