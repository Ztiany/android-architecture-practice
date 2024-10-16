package com.app.base.compose.dialog.loading

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.base.compose.dialog.facility.DialogStyle

@Composable
fun LoadingDialog(
    modifier: Modifier = Modifier,
    state: LoadingDialogState = rememberLoadingDialogState(),
    dialogStyle: DialogStyle = DialogStyle.defaultImpl(),
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ),
) {
    if (!state.show) {
        return
    }

    Dialog(
        onDismissRequest = { state.hide() },
        properties = properties
    ) {
        LoadingIndicator(
            modifier = modifier,
            loadingState = state,
            dialogStyle = dialogStyle
        )
    }
}