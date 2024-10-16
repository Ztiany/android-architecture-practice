package com.app.base.compose.dialog.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.base.compose.design.theme.AppTheme
import com.app.base.compose.dialog.facility.DialogFrame
import com.app.base.compose.dialog.facility.DialogStyle

@Composable
internal fun LoadingIndicator(
    modifier: Modifier = Modifier,
    loadingState: LoadingDialogState,
    dialogStyle: DialogStyle,
) {
    DialogFrame(
        modifier = modifier,
        dialogStyle = dialogStyle,
    ) {
        Column {
            Text(
                text = loadingState.label.value,
                modifier = Modifier.wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}