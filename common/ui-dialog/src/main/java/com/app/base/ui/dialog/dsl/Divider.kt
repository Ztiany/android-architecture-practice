package com.app.base.ui.dialog.dsl

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.android.base.utils.android.views.setHeight
import com.google.android.material.color.MaterialColors

class Divider(private val context: Context) {

    @ColorInt
    private var _color: Int = Color.BLACK

    /** unit: px */
    private var _thickness: Int = 1

    fun color(@ColorInt color: Int) {
        this._color = color
    }

    fun colorAttr(@AttrRes colorAttr: Int) {
        this._color = MaterialColors.getColor(context, colorAttr, "dividerColorAttr is not found in your theme.")
    }

    fun thickness(thickness: Int) {
        this._thickness = thickness
    }

    fun toDividerDescription(): DividerDescription {
        return DividerDescription(_color, _thickness)
    }

}

class DividerDescription(
    val color: Int,
    val thickness: Int,
)

fun DividerDescription.applyTo(divider: View) {
    divider.setBackgroundColor(color)
    divider.setHeight(thickness)
}
