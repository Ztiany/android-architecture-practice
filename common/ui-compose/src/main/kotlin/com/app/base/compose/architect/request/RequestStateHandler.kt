package com.app.base.compose.architect.request

import androidx.compose.runtime.Composable
import com.android.base.foundation.state.Data
import com.android.base.foundation.state.Error
import com.android.base.foundation.state.Loading
import com.android.base.foundation.state.NoData
import com.android.base.foundation.state.State
import com.android.base.foundation.state.Success
import com.app.base.compose.architect.HandlingProcedure

/** Configure how to handle a request state: [State]. */
class StateHandlerBuilder<L, D, E> internal constructor() {

    internal var onLoading: @Composable ((Loading<L, D>) -> Unit)? = null
    internal var onLoadingEnd: (() -> Unit)? = null
    internal var onIdle: (() -> Unit)? = null

    // act like an event
    internal var onError: (HandlingProcedure.(Error<E, D>) -> Unit)? = null
    internal var onSuccess: ((D?) -> Unit)? = null
    internal var onData: ((D) -> Unit)? = null
    internal var onNoData: (() -> Unit)? = null

    // act like a state
    internal var onErrorState: (HandlingProcedure.(Error<E, D>) -> Unit)? = null
    internal var onSuccessState: ((D?) -> Unit)? = null
    internal var onDataState: ((D) -> Unit)? = null
    internal var onNoDataState: (() -> Unit)? = null

    /** The [block] will be called once state is [Loading]. */
    fun onLoading(block: @Composable (Loading<L, D>) -> Unit) {
        this.onLoading = block
    }

    /** The [block] will be called once state is not [Loading].. */
    fun onLoadingEnd(block: () -> Unit) {
        this.onLoadingEnd = block
    }

    /** The [block] will be called when [State] is [Error] and is not handled. It behaves like an event. */
    fun onError(block: HandlingProcedure.(Error<E, D>) -> Unit) {
        this.onError = block
    }

    /** The [block] will be called once [State] is [Error]. */
    fun onErrorState(block: HandlingProcedure.(Error<E, D>) -> Unit) {
        this.onErrorState = block
    }

    /** The [block] will always be called when [State] is [Success] and is not handled. It behaves like an event. */
    fun onSuccess(block: (D?) -> Unit) {
        this.onSuccess = block
    }

    /** The [block] will be called only when [State] is [Data] and is not handled. It behaves like an event. It behaves like an event. */
    fun onData(block: (D) -> Unit) {
        this.onData = block
    }

    /** The [block] will be called only when [State] is [NoData] and is not handled. It behaves like an event. */
    fun onNoData(block: () -> Unit) {
        this.onNoData = block
    }

    /** The [block] will always be called once [State] is [Success]. */
    fun onSuccessState(block: (D?) -> Unit) {
        this.onSuccessState = block
    }

    /** The [block] will be called only when [State] is [Data]. */
    fun onDataState(block: (D) -> Unit) {
        this.onDataState = block
    }

    /** The [block] will be called only when [State] is [NoData]. */
    fun onNoDataState(block: () -> Unit) {
        this.onNoDataState = block
    }

}