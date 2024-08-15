package com.app.base.ui.widget.insets

import android.app.Activity
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.base.utils.android.compat.SystemBarCompat
import com.google.android.material.color.MaterialColors

fun View.fitSystemBarsInsets(
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

fun Activity.setStatusBarColorSameWithWindow() {
    SystemBarCompat.setStatusBarColor(
        this,
        MaterialColors.getColor(this, android.R.attr.windowBackground, "windowBackground not provided.")
    )
}

fun Activity.setNavigatorBarColorSameWithWindow() {
    SystemBarCompat.setNavigationBarColor(
        this,
        MaterialColors.getColor(this, android.R.attr.windowBackground, "windowBackground not provided.")
    )
}