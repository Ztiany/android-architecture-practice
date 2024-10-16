package com.app.base.compose.architect.request

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.android.base.core.AndroidSword
import com.android.base.foundation.state.Data
import com.android.base.foundation.state.Error
import com.android.base.foundation.state.Idle
import com.android.base.foundation.state.Loading
import com.android.base.foundation.state.NoData
import com.android.base.foundation.state.State
import com.android.base.foundation.state.Success
import com.app.base.compose.architect.HandlingProcedure
import kotlinx.coroutines.delay

@Composable
fun <L, D, E> RequestStateHandler(
    state: androidx.compose.runtime.State<State<L, D, E>>,
    handlerBuilder: StateHandlerBuilder<L, D, E>.() -> Unit,
) {
    val stateHandler = remember { StateHandlerBuilder<L, D, E>() }
    stateHandler.handlerBuilder()
    InternalRequestStateHandler({ state.value }, stateHandler)
}

@Composable
fun <L, D, E> RequestStateHandler(
    stater: () -> State<L, D, E>,
    handlerBuilder: StateHandlerBuilder<L, D, E>.() -> Unit,
) {
    val stateHandler = remember { StateHandlerBuilder<L, D, E>() }
    stateHandler.handlerBuilder()
    InternalRequestStateHandler(stater, stateHandler)
}

@Composable
fun <L, D, E> InternalRequestStateHandler(
    stater: () -> State<L, D, E>,
    stateHandler: StateHandlerBuilder<L, D, E>,
) {

    when (val state = stater()) {
        is Idle -> {
            stateHandler.onIdle?.invoke()
        }

        //----------------------------------------loading start
        // The loading state should always be handled, so we ignore the clearAfterHanded config here.
        is Loading -> {
            stateHandler.onLoading?.invoke(state)
        }
        //----------------------------------------loading end

        //----------------------------------------error start
        is Error -> {
            LaunchedEffect(state) {
                delay(AndroidSword.minimalDialogDisplayTime)

                stateHandler.onLoadingEnd?.invoke()
                if (stateHandler.onError != null || stateHandler.onErrorState != null) {
                    val procedure = HandlingProcedure {
                        AndroidSword.requestErrorHandler.handle(state.error)
                    }
                    if (!state.isHandled) {
                        stateHandler.onError?.invoke(procedure, state)
                    }
                    stateHandler.onErrorState?.invoke(procedure, state)
                } else if (!state.isHandled) {
                    AndroidSword.requestErrorHandler.handle(state.error)
                }
                state.markAsHandled()
            }
        }
        //----------------------------------------error end

        //----------------------------------------success start
        is Success<D> -> {
            LaunchedEffect(state) {
                stateHandler.onLoadingEnd?.invoke()
                processOnSuccess(state, stateHandler.onSuccess, stateHandler.onData, stateHandler.onNoData, true)
                processOnSuccess(state, stateHandler.onSuccessState, stateHandler.onDataState, stateHandler.onNoDataState, false)
                state.markAsHandled()
            }
        }
        //----------------------------------------success end

    }
}

private fun <D> processOnSuccess(
    state: Success<D>,
    onSuccess: ((D?) -> Unit)?,
    onData: ((D) -> Unit)?,
    onNoData: (() -> Unit)?,
    asEvent: Boolean,
) {
    if (asEvent && state.isHandled) {
        return
    }
    when (state) {
        is NoData -> {
            onSuccess?.invoke(null)
            onNoData?.invoke()
        }

        is Data<D> -> {
            onSuccess?.invoke(state.value)
            onData?.invoke(state.value)
        }
    }
}