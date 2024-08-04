@file:JvmName("Sizes")

package com.app.base.ui.widget

import android.content.Context
import android.util.TypedValue
import android.view.View

context(View)
        internal fun Int.dip(): Int = with(context) { dpToPx() }

context(View)
        internal fun Float.dip(): Float = with(context) { dpToPx() }

context(View)
        internal fun Int.sp(): Int = with(context) { spToPx() }

context(View)
        internal fun Float.sp(): Float = with(context) { spToPx() }

context(Context)
        internal fun Float.dpToPx(): Float {
    return this * resources.displayMetrics.density
}

context(Context)
        internal fun Int.dpToPx(): Int {
    return (this * resources.displayMetrics.density + 0.5f).toInt()
}

context(Context)
        internal fun Float.pxToDp(): Float {
    return this / resources.displayMetrics.density
}

context(Context)
        internal fun Int.pxToDp(): Int {
    return (this / resources.displayMetrics.density + 0.5f).toInt()
}

context(Context)
        internal fun Float.spToPx(): Float {
    return this * resources.displayMetrics.scaledDensity
}

context(Context)
        internal fun Int.spToPx(): Int {
    return (this * resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

context(Context)
        internal fun Float.pxToSp(): Float {
    return this / resources.displayMetrics.scaledDensity
}

context(Context)
        internal fun Int.pxToSp(): Int {
    return (this / resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

context(Context)
        internal fun Float.applyDimension(unit: Int): Float {
    val metrics = resources.displayMetrics
    when (unit) {
        TypedValue.COMPLEX_UNIT_PX -> return this
        TypedValue.COMPLEX_UNIT_DIP -> return this * metrics.density
        TypedValue.COMPLEX_UNIT_SP -> return this * metrics.scaledDensity
        TypedValue.COMPLEX_UNIT_PT -> return this * metrics.xdpi * (1.0F / 72)
        TypedValue.COMPLEX_UNIT_IN -> return this * metrics.xdpi
        TypedValue.COMPLEX_UNIT_MM -> return this * metrics.xdpi * (1.0F / 25.4F)
    }
    return 0F
}
