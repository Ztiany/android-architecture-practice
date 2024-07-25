package com.app.base.utils.state

import com.android.base.fragment.state.SimpleDataState
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.onError
import com.android.sdk.net.coroutines.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> (suspend () -> CallResult<T>).transformToSimpleDataStateFlow(): Flow<SimpleDataState<T>> {
    return flow {
        emit(SimpleDataState(isRefreshing = true))
        invoke() onSuccess {
            emit(SimpleDataState(data = it))
        } onError {
            emit(SimpleDataState(refreshError = it))
        }
    }
}
