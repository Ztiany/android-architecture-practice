package com.app.base.ui.widget.insets

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.android.base.utils.android.compat.SystemBarCompat
import com.android.base.utils.android.compat.doBeforeSDK
import com.android.base.utils.android.compat.doFromSDK
import com.android.base.utils.android.compat.doInSDKRange
import com.android.base.utils.android.compat.setLayoutExtendsToSystemBars
import com.android.base.utils.android.compat.setNavigationBarColor
import com.android.base.utils.android.compat.setNavigationBarLightMode
import com.android.base.utils.android.compat.setStatusBarLightMode
import com.android.base.utils.android.views.getStyledColor
import com.app.base.ui.theme.R
import com.google.android.material.color.MaterialColors

/**
 * Enable edge to edge compatible for the activity.
 *
 * @param navigationBarColor the color of the navigation bar. The default value is defined by theme attribute `app_color_lightest`.
 * @param forcedNavigationBarColor when set to true, the navigation bar will be forced to use the color defined by `navigationBarColor`.
 * Be careful to use this option. When an app runs before API 26 and sets the navigation bar color to a light color, users may be disturbed
 * by the light navigation bar.
 */
fun Activity.enableEdgeToEdgeCompatible(
    navigationBarColor: Int? = getStyledColor(R.attr.app_color_lightest, "app_color_lightest not provided."),
    forcedNavigationBarColor: Boolean = false,
) {
    setLayoutExtendsToSystemBars()
    adjustSystemBarColorInEdge2EdgeMode(navigationBarColor, forcedNavigationBarColor)
}

private fun Activity.adjustSystemBarColorInEdge2EdgeMode(navigationBarColor: Int?, forcedNavigationBarColor: Boolean) {
    // adjust the status bar style when edge to edge enabled.
    setStatusBarLightMode()
    // adjust the navigation bar style when edge to edge enabled.
    // Case 1: As of API 29, NavigationBar has been adjusted to a tiny bar. It will handle its style.
    if (navigationBarColor != null) {
        // The default background color of the NavigationBar is the same as the window background color.
        // There some pager whose background color is not the same as the window background color,
        // So we need to set the NavigationBar's color to the color defined by `app_color_lightest` to make it looks better.
        if (forcedNavigationBarColor) {
            setNavigationBarColor(navigationBarColor)
        } else {
            doFromSDK(26) {
                setNavigationBarColor(navigationBarColor)
            }
        }
    }
    // Case 3: NavigationBar introduced the day/night mode as of API 26. Just use it.
    doInSDKRange(26, 28) {
        setNavigationBarLightMode()
    }
    // Case 3: Before api 26, the NavigationBar has no day/night mode, so we have to set a scrim for it.
    if (!forcedNavigationBarColor) {
        doBeforeSDK(26) {
            setNavigationBarColor(getStyledColor(R.attr.app_color_deepest_opacity20, "app_color_deepest_opacity20"))
        }
    }
}

fun View.fitsSystemBarsInsetsWithPadding(
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false,
) {
    val originalPadding = intArrayOf(paddingLeft, paddingTop, paddingRight, paddingBottom)

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(
            if (left) systemBars.left + originalPadding[0] else originalPadding[0],
            if (top) systemBars.top + originalPadding[1] else originalPadding[1],
            if (right) systemBars.right + originalPadding[2] else originalPadding[2],
            if (bottom) systemBars.bottom + originalPadding[3] else originalPadding[3]
        )
        insets
    }
}

inline fun <reified T : MarginLayoutParams> View.fitsSystemBarsInsetsWithMargin(
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false,
) {
    val lp = layoutParams as? MarginLayoutParams ?: return
    val originalMargin = intArrayOf(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin)

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.updateLayoutParams<T> {
            leftMargin = if (left) systemBars.left + originalMargin[0] else originalMargin[0]
            topMargin = if (top) systemBars.top + originalMargin[1] else originalMargin[1]
            rightMargin = if (right) systemBars.right + originalMargin[2] else originalMargin[2]
            bottomMargin = if (bottom) systemBars.bottom + originalMargin[3] else originalMargin[3]
        }
        insets
    }
}

fun View.onApplySystemBarsInsets(
    onInsets: (Insets) -> Unit,
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        onInsets(systemBars)
        insets
    }
}

fun Activity.setStatusBarColorSameWithWindow() {
    SystemBarCompat.setStatusBarColor(
        this,
        MaterialColors.getColor(this, android.R.attr.windowBackground, "windowBackground not provided.")
    )
}

fun Activity.setStatusBarColorLightest() {
    SystemBarCompat.setStatusBarColor(
        this,
        MaterialColors.getColor(this, R.attr.app_color_lightest, "app_color_lightest not provided.")
    )
}

fun Activity.setNavigationBarColorCompatible(
    navigationBarColor: Int,
    forcedNavigationBarColor: Boolean = false,
) {
    if (forcedNavigationBarColor) {
        setNavigationBarColor(navigationBarColor)
    } else {
        doFromSDK(26) {
            setNavigationBarColor(navigationBarColor)
        }
        doBeforeSDK(26) {
            setNavigationBarColor(getStyledColor(R.attr.app_color_deepest_opacity20, "app_color_deepest_opacity20"))
        }
    }
}

fun Activity.setNavigationBarColorSameWithWindow() {
    setNavigationBarColorCompatible(getStyledColor(android.R.attr.windowBackground, "android.R.attr.windowBackground"))
}

/**
 * If your page is edge to edge, you can use this method to set the NavigationBar's color to the lightest color, instead of [setNavigationBarColorLightest].
 */
fun Activity.setNavigationBarColorLightest() {
    setNavigationBarColorCompatible(getStyledColor(R.attr.app_color_lightest, "app_color_lightest"))
}

fun Activity.adjustSystemBarColorInNormalMode() {
    // the StatusBar‘s background color is lightest_color(which is white in the day theme).
    // StatusBar's day/night mode takes effect as of API 21.
    setStatusBarLightMode()
    // the default NavigationBar‘s background color is the same as window background color.
    // NavigationBar's day/night mode takes effect as of API 26.
    doInSDKRange(26, 28) {
        setNavigationBarLightMode()
    }
    // Before api 26, the NavigationBar has no day/night mode, so we have to set a scrim for it, or users may be disturbed by the light navigation bar.
    doBeforeSDK(26) {
        setNavigationBarColor(getStyledColor(R.attr.app_color_deepest_opacity20, "app_color_deepest_opacity20"))
    }
}

fun Activity.setNavigationBarTransparent() {
    setNavigationBarColor(Color.TRANSPARENT)
}