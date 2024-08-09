package com.app.base.ui.dialog.dsl

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.google.android.material.color.MaterialColors

class PopupDim(private val context: Context) {

    private var _dimPercent = 0.3F

    private var _dimView: View? = null

    private var _dimColor: Int = Color.TRANSPARENT

    private var _includeNavigationBar = true

    /** 使用系统的 dim 属性，如果设置了此属性，那么 [dimView] 不会生效。 */
    fun systemDim(percent: Float) {
        _dimPercent = percent
        _dimView = null
    }

    fun dimView(view: View, @ColorInt color: Int) {
        _dimView = view
        _dimColor = color
        _dimPercent = 0F
    }

    fun dimViewWithAttr(view: View, @AttrRes color: Int) {
        _dimView = view
        _dimColor = MaterialColors.getColor(context, color, "dimViewAttr is not found in your theme.")
    }

    fun includeNavigationBar(include: Boolean) {
        _includeNavigationBar = include
    }

    fun disable() {
        _dimView = null
        _dimPercent = PopupDimDescription.NO_DIM
    }

    fun toPopupDimDescription(): PopupDimDescription {
        return PopupDimDescription(
            includeNavigationBar = _includeNavigationBar,
            dimPercent = _dimPercent,
            dimView = _dimView,
            dimColor = _dimColor,
        )
    }

}

class PopupDimDescription(
    val includeNavigationBar: Boolean,
    val dimPercent: Float,
    val dimView: View?,
    val dimColor: Int,
) {

    companion object {
        internal const val NO_DIM = -1F
    }
}