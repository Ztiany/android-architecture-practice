package com.app.base.compose.design.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import com.app.base.compose.design.theme.AppColorScheme
import com.app.base.compose.design.theme.AppTheme

/**
 * 主色调渐变按钮，默认为圆角，通过 [shape] 参数可以自定义。
 */
@Composable
fun PrimaryGradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    strictMinimumInteractiveSize: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    BrushButton(
        modifier = modifier,
        onClick = onClick,
        colors = AppTheme.colors.primaryGradientButtonColors(),
        enabled = enabled,
        shape = shape,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        strictMinimumInteractiveSize = strictMinimumInteractiveSize,
        content = content
    )
}

fun AppColorScheme.primaryGradientButtonColors(): BrushButtonColors {
    return cachedPrimaryGradientButtonColors ?: BrushButtonColors(
        containerBrush = Brush.horizontalGradient(
            colors = listOf(
                primaryBright,
                primary,
            )
        ),
        disabledContainerBrush = SolidColor(textLevel1.copy(alpha = AppButtonDefaults.DisabledContainerOpacity)),
        contentColor = lightest,
        disabledContentColor = textLevel1.copy(alpha = AppButtonDefaults.DisabledLabelTextOpacity),
    ).also {
        cachedPrimaryGradientButtonColors = it
    }
}