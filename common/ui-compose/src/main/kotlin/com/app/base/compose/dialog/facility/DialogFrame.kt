package com.app.base.compose.dialog.facility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun DialogFrame(
    modifier: Modifier = Modifier,
    dialogStyle: DialogStyle = DialogStyle.defaultImpl(),
    body: @Composable () -> Unit,
) {

    val configuration = LocalConfiguration.current

    Surface(
        color = dialogStyle.background.color,
        modifier = modifier
            .clip(dialogStyle.background.shape)
            .background(dialogStyle.background.color)
            .widthIn(max = dialogStyle.size.width(configuration))
            .fillMaxWidth()
            .run {
                val height = dialogStyle.size.height(configuration)
                if (height == 0.dp) {
                    wrapContentHeight()
                } else {
                    heightIn(max = height)
                }
            }
            .padding(dialogStyle.contentPadding),
    ) {
        body()
    }
}