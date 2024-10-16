package com.app.base.compose.design.component.divider

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = AppDividerDefaults.Thickness,
    color: Color = AppDividerDefaults.Color,
) {
    androidx.compose.material3.HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = AppDividerDefaults.Thickness,
    color: Color = AppDividerDefaults.Color,
) {
    androidx.compose.material3.VerticalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}