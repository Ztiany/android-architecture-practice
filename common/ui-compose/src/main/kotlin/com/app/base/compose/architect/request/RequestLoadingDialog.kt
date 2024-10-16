package com.app.base.compose.architect.request

import androidx.compose.runtime.Composable
import com.app.base.compose.dialog.loading.LoadingDialog
import com.app.base.compose.dialog.loading.LoadingDialogState
import com.app.base.compose.dialog.loading.rememberLoadingDialogState

@Composable
fun RequestLoadingDialog(state: LoadingDialogState = rememberLoadingDialogState(show = true)) {
    LoadingDialog(state = state)
}