package com.app.base.compose.dialog.alert

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class AlertDialogState internal constructor(
    show: Boolean,
    private val onDismiss: ((byUser: Boolean) -> Unit)?,
) {

    internal var show by mutableStateOf(show)

    fun show() {
        show = true
    }

    fun hide() {
        onDismiss?.invoke(false)
        show = false
    }

    fun hideOnRequest() {
        onDismiss?.invoke(true)
        show = false
    }

}

@Composable
fun rememberAlertDialogState(
    show: Boolean = false,
    onDismiss: ((byUser: Boolean) -> Unit)? = null,
): AlertDialogState {
    return remember {
        AlertDialogState(show, onDismiss)
    }
}