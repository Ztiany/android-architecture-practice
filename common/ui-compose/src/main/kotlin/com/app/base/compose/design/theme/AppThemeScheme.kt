package com.app.base.compose.design.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.app.base.compose.design.component.button.BrushButtonColors

@Immutable
data class AppColorScheme(
    val primary: Color,
    val primaryDeep: Color,
    val primaryBright: Color,
    val divider: Color,
    val lightest: Color,
    val deepest: Color,
    val textLevel1: Color,
    val textLevel2: Color,
    val textLevel3: Color,
    val textLevel4: Color,
    val textLevel5: Color,
) {

    internal var cachedPrimaryGradientButtonColors: BrushButtonColors? = null

}