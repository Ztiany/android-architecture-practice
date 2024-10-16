package com.app.base.compose.dialog.facility

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.base.compose.design.theme.AppTheme

data class DialogSize(
    val width: (Configuration) -> Dp,
    val height: (Configuration) -> Dp,
) {

    companion object {
        fun defaultImpl() = DialogSize(
            width = { configuration ->
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    configuration.screenWidthDp.dp * 0.75F
                } else {
                    configuration.screenWidthDp.dp * 0.45F
                }
            },
            height = { 0.dp },
        )
    }
}

data class DialogBackground(
    val color: Color,
    val shape: Shape = RectangleShape,
) {

    companion object {
        @Composable
        fun defaultImpl() = DialogBackground(
            color = AppTheme.colors.lightest,
            shape = RoundedCornerShape(8.dp),
        )
    }
}

data class DialogStyle(
    val size: DialogSize,
    val background: DialogBackground,
    val contentPadding: PaddingValues,
) {
    companion object {
        @Composable
        fun defaultImpl() = DialogStyle(
            size = DialogSize.defaultImpl(),
            background = DialogBackground.defaultImpl(),
            contentPadding = PaddingValues(16.dp),
        )
    }
}