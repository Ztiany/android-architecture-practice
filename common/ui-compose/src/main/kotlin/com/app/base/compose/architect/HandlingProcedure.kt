package com.app.base.compose.architect


class HandlingProcedure internal constructor(
    private val defaultHandling: (() -> Unit)? = null,
) {

    fun continueDefaultProcedure() {
        defaultHandling?.invoke()
    }

}