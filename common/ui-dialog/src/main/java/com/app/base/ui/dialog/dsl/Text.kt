package com.app.base.ui.dialog.dsl

import android.app.Activity
import android.graphics.Color
import com.google.android.material.color.MaterialColors

open class Text internal constructor(private val activity: Activity, val text: CharSequence) {

    internal var textColor: Int = Color.BLACK

    /** unit: sp */
    internal var textSize: Float = 16F

    internal var textGravity: Int = android.view.Gravity.CENTER

    internal var clickableSpan: Boolean = false

    fun color(color: Int) {
        textColor = color
    }

    fun attrColor(attr: Int) {
        textColor = MaterialColors.getColor(activity, attr, "textColor not found in your theme!")
    }

    fun gravity(gravity: Int) {
        textGravity = gravity
    }

    /** unit: sp */
    fun size(size: Float) {
        textSize = size
    }

    fun asClickableSpan() {
        clickableSpan = true
    }

}