package com.app.base.utils

import androidx.lifecycle.MutableLiveData
import com.android.base.foundation.state.StateD
import com.android.base.foundation.state.emitData
import com.android.base.foundation.state.emitError
import com.android.base.foundation.state.emitLoading
import com.android.base.foundation.state.setData
import com.android.base.foundation.state.setError
import com.android.base.foundation.state.setLoading
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.onError
import com.android.sdk.net.coroutines.onSuccess
import com.app.common.api.errorhandler.ErrorHandler
import kotlinx.coroutines.flow.MutableSharedFlow
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

suspend fun <T> CallResult<T>.intoSharedFlow(flow: MutableSharedFlow<StateD<T>>) {
    onSuccess {
        flow.emitData(it)
    }
    onError {
        flow.emitError(it)
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

suspend fun <T> MutableSharedFlow<StateD<T>>.syncApiCallState(call: suspend () -> CallResult<T>) {
    emitLoading()
    call.invoke().intoSharedFlow(this)
}

suspend fun <T> MutableStateFlow<StateD<T>>.syncApiCallState(call: suspend () -> CallResult<T>) {
    setLoading()
    call.invoke().intoState(this)
}

suspend fun <T> MutableLiveData<StateD<T>>.syncApiCallState(call: suspend () -> CallResult<T>) {
    call.invoke().intoLiveData(this)
}