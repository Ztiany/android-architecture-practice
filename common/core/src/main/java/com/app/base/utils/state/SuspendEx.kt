package com.app.base.utils.state

import com.android.base.foundation.state.StateD
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import timber.log.Timber

fun <T> (suspend () -> T).transformToStateFlowCaught(): Flow<StateD<T>> {
    return flow {
        emit(StateD.loading())
        try {
            emit(StateD.success(invoke()))
        } catch (e: Exception) {
            Timber.e(e, "transformToStateFlow")
            if (currentCoroutineContext().isActive) {
                emit(StateD.error(e))
            }
        }
    }
}

fun <T> (suspend () -> T).transformToStateFlowUnCaught(): Flow<StateD<T>> {
    return flow {
        emit(StateD.loading())
        emit(StateD.success(invoke()))
    }
}