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
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import timber.log.Timber

fun <T> CallResult<T>.noticeOnError(errorHandler: ErrorHandler) {
    onError {
        errorHandler.handleError(it)
    }
}

suspend fun <T> CallResult<T>.intoFlowCollector(collector: FlowCollector<StateD<T>>) {
    onSuccess {
        collector.emit(StateD.success(it))
    }
    onError {
        collector.emit(StateD.error(it))
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

suspend fun <T> FlowCollector<StateD<T>>.syncApiCallState(call: suspend () -> CallResult<T>) {
    emit(StateD.loading())
    call.invoke().intoFlowCollector(this)
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

fun <T> (suspend () -> CallResult<T>).callIntoFlow(): Flow<StateD<T>> {
    return flow {
        emit(StateD.loading())
        invoke().intoFlowCollector(this)
    }
}

fun <T> (suspend () -> T).executionIntoFlow(): Flow<StateD<T>> {
    return flow {
        emit(StateD.loading())
        try {
            emit(StateD.success(invoke()))
        } catch (e: Exception) {
            Timber.d("intoFlow")
            if (currentCoroutineContext().isActive) {
                emit(StateD.error(e))
            }
        }
    }
}