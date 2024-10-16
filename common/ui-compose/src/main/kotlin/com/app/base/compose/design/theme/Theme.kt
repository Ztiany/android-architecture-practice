package com.app.base.compose.design.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val PrimaryBright = Color(0xFFFC8C5A)
private val Primary = Color(0xFFF26527)
private val PrimaryDeep = Color(0xFFF84802)
private val Lightest = Color.White
private val Deepest = Color.Black
private val TextLevel1 = Color(0xFF333333)
private val TextLevel2 = Color(0xFF666666)
private val TextLevel3 = Color(0xFF999999)
private val TextLevel4 = Color(0xFFA4A4A4)
private val TextLevel5 = Color(0xFFCCCCCC)
private val Background = Color(0xFFF3F3F3)
private val Surface = Color(0xFFF5F5F5)
private val Divider = Color(0xFFE0E0E0)

/**
 * For more information, see: [MD Color Definition](https://m3.material.io/styles/color/roles).
 */
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFF26527),
    onPrimary = Lightest,
    // primaryContainer = ,
    // onPrimaryContainer = ,
    // inversePrimary = Color.Magenta,

    secondary = Color(0xFF5993F2),
    onSecondary = Lightest,
    // secondaryContainer = debugColor,
    // onSecondaryContainer = debugColor,

    tertiary = Color(0xFFF44766),
    onTertiary = Lightest,
    // tertiaryContainer = ,
    // onTertiaryContainer = ,

    background = Background,
    onBackground = TextLevel1,

    surface = Surface,
    onSurface = TextLevel1,
    // surfaceBright = ,
    // surfaceDim = ,
    // surfaceContainer = ,
    // surfaceContainerHigh = ,
    // surfaceContainerHighest = ,
    // surfaceContainerLow = ,
    // surfaceContainerLowest = ,
    // surfaceTint = ,
    // surfaceVariant = ,
    // onSurfaceVariant = ,
    // inverseSurface = ,
    // inverseOnSurface = ,

    // error = ,
    // onError = ,
    // errorContainer = ,
    // onErrorContainer = ,

    // scrim = ,

    // outline = ,
    // outlineVariant = ,
)

// TODO: implement dark color scheme.
private val DarkColorScheme = darkColorScheme()

private val LightAppColorScheme = AppColorScheme(
    primary = Primary,
    primaryDeep = PrimaryDeep,
    primaryBright = PrimaryBright,

    divider = Divider,

    lightest = Lightest,
    deepest = Deepest,

    textLevel1 = TextLevel1,
    textLevel2 = TextLevel2,
    textLevel3 = TextLevel3,
    textLevel4 = TextLevel4,
    textLevel5 = TextLevel5,
)

// TODO: implement dark color scheme.
private val DarkAppColorScheme = LightAppColorScheme

private val LocalAppColors = staticCompositionLocalOf<AppColorScheme> {
    error("No AppColorScheme provided")
}

object AppTheme {
    val colors: AppColorScheme
        @Composable
        get() = LocalAppColors.current
}

/**
 * For configure theme and migration, see:
 *
 *  - [Migrate XML themes to Compose](https://developer.android.com/develop/ui/compose/designsystems/views-to-compose)
 *  - [Material Design 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)
 *  - [Migrate from Material 2 to Material 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material2-material3)
 *
 *  For customizing and extending your theme, refers to:
 *
 *  - [Jetsnack app](https://github.com/android/compose-samples/blob/main/Jetsnack/app/src/main/java/com/example/jetsnack/ui/theme/Theme.kt)
 */
@Composable
fun EkAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val colors = if (darkTheme) DarkAppColorScheme else LightAppColorScheme

    CompositionLocalProvider(
        // AppColorScheme provides colors
        LocalAppColors provides colors
    ) {
        // Material3 theme
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}