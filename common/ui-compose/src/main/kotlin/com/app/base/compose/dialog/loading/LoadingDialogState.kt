package com.app.base.compose.dialog.loading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource

class LoadingDialogState(
    label: String,
    show: Boolean,
    private val onDismiss: (() -> Unit)?,
) {

    internal val label = mutableStateOf(label)

    internal var show by mutableStateOf(show)

    fun updateLabel(label: String) {
        this.label.value = label
    }

    /**
     * Display the dialog / view.
     */
    fun show() {
        show = true
    }

    /**
     * Hide the dialog / view.
     */
    fun hide() {
        show = false
        onDismiss?.invoke()
    }

}

@Composable
fun rememberLoadingDialogState(
    label: String = stringResource(id = com.app.base.ui.theme.R.string.dialog_loading),
    show: Boolean = false,
    onDismissRequest: (() -> Unit)? = null,
) = remember {
    LoadingDialogState(label, show, onDismissRequest)
}