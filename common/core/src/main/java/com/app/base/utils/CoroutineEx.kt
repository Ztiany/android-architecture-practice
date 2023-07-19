package com.app.base.utils

import kotlinx.coroutines.CancellationException


fun Throwable.rethrowIfCancellation() {
    if (this is CancellationException) {
        throw this
    }
}