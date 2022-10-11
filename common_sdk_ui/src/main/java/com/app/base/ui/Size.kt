@file:JvmName("Sizes")

package com.app.base.ui

import android.content.Context
import android.util.TypedValue
import android.view.View

internal fun View.dip(value: Int): Int = dpToPx(context, value)
internal fun View.dip(value: Float): Float = dpToPx(context, value)
internal fun View.sp(value: Int): Int = spToPx(context, value)
internal fun View.sp(value: Float): Float = spToPx(context, value)

internal fun dpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}

internal fun dpToPx(context: Context, dp: Int): Int {
    return (dp * context.resources.displayMetrics.density + 0.5f).toInt()
}

internal fun pxToDp(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}

internal fun pxToDp(context: Context, px: Int): Int {
    return (px / context.resources.displayMetrics.density + 0.5f).toInt()
}

internal fun spToPx(context: Context, sp: Float): Float {
    return sp * context.resources.displayMetrics.scaledDensity
}

internal fun spToPx(context: Context, sp: Int): Int {
    return (sp * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

internal fun pxToSp(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.scaledDensity
}

internal fun pxToSp(context: Context, px: Int): Int {
    return (px / context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

internal fun applyDimension(context: Context, unit: Int, value: Float): Float {
    val metrics = context.resources.displayMetrics
    when (unit) {
        TypedValue.COMPLEX_UNIT_PX -> return value
        TypedValue.COMPLEX_UNIT_DIP -> return value * metrics.density
        TypedValue.COMPLEX_UNIT_SP -> return value * metrics.scaledDensity
        TypedValue.COMPLEX_UNIT_PT -> return value * metrics.xdpi * (1.0F / 72)
        TypedValue.COMPLEX_UNIT_IN -> return value * metrics.xdpi
        TypedValue.COMPLEX_UNIT_MM -> return value * metrics.xdpi * (1.0F / 25.4F)
    }
    return 0f
}
