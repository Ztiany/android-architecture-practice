package com.app.base.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.foundation.state.StateD
import com.android.base.foundation.state.setData
import com.android.base.foundation.state.setError
import com.android.base.foundation.state.setLoading
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.onError
import com.android.sdk.net.coroutines.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launch(context, start, block)
}

fun <T> ViewModel.apiCallStateIntoLiveData(
    state: MutableLiveData<StateD<T>>,
    call: suspend () -> CallResult<T>,
) = launch {
    state.setLoading()
    call() onSuccess {
        state.setData(it)
    } onError {
        state.setError(it)
    }
}

fun <T> ViewModel.apiCallStateIntoFlow(
    state: MutableStateFlow<StateD<T>>,
    call: suspend () -> CallResult<T>,
) = launch {
    state.setLoading()
    call() onSuccess {
        state.setData(it)
    } onError {
        state.setError(it)
    }
}