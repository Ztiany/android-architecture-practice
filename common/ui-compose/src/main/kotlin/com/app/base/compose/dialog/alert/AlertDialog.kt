package com.app.base.compose.dialog.alert

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.base.compose.dialog.facility.DialogStyle

@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
    positiveButton: @Composable (() -> Unit)? = null,
    negativeButton: @Composable (() -> Unit)? = null,
    state: AlertDialogState = rememberAlertDialogState(),
    dialogStyle: DialogStyle = DialogStyle.defaultImpl().copy(contentPadding = PaddingValues(0.dp)),
    properties: DialogProperties = DialogProperties(),
) {
    if (!state.show) {
        return
    }

    Dialog(
        onDismissRequest = { state.hideOnRequest() },
        properties = properties
    ) {
        AlertDialogContent(
            modifier = modifier,
            content = content,
            title = title,
            positiveButton = positiveButton,
            negativeButton = negativeButton,
            dialogStyle = dialogStyle
        )
    }
}

@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleStyle: TextStyle = AlertDialogDefaults.DefaultTitleStyle,
    message: String,
    messageStyle: TextStyle = AlertDialogDefaults.DefaultMessageStyle,
    positiveButton: String? = null,
    onPositiveClick: (() -> Unit)? = null,
    negativeButton: String? = null,
    onNegativeClick: (() -> Unit)? = null,
    state: AlertDialogState = rememberAlertDialogState(),
    dialogStyle: DialogStyle = DialogStyle.defaultImpl().copy(contentPadding = PaddingValues(0.dp)),
    autoDismiss: Boolean = true,
    properties: DialogProperties = DialogProperties(),
) {
    if (!state.show) {
        return
    }

    Dialog(
        onDismissRequest = { state.hideOnRequest() },
        properties = properties
    ) {
        AlertDialogContent(
            modifier = modifier,
            title = title,
            titleStyle = titleStyle,
            message = message,
            messageStyle = messageStyle,
            positiveButton = positiveButton,
            onPositiveClick = {
                if (autoDismiss) {
                    state.hide()
                }
                onPositiveClick?.invoke()
            },
            negativeButton = negativeButton,
            onNegativeClick = {
                if (autoDismiss) {
                    state.hide()
                }
                onNegativeClick?.invoke()
            },
            dialogStyle = dialogStyle
        )
    }
}