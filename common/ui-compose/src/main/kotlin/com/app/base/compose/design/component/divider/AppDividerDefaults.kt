package com.app.base.compose.design.component.divider

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.app.base.compose.design.theme.AppTheme

object AppDividerDefaults {

    /** Default thickness of a divider. */
    val Thickness: Dp @Composable get() = with(LocalDensity.current) { 1.toDp() }

    /** Default color of a divider. */
    val Color: Color @Composable get() = AppTheme.colors.divider

}