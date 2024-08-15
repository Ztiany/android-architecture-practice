package com.app.base.ui.widget.insets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.use
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.base.ui.widget.R

class InsetsCompatHelper(
    private val context: Context,
    private val attrs: AttributeSet?,
    private val defaultStyleAttr: Int = 0,
    private val defaultStyleRes: Int = 0,
) {

    private var direction = 0
    private var type = 0

    private var paddingStart = 0
    private var paddingTop = 0
    private var paddingEnd = 0
    private var paddingBottom = 0

    init {
        withStyleable(R.styleable.InsetCompatLayout) {
            direction = getInt(R.styleable.InsetCompatLayout_iclDirection, 0)
            type = getInt(R.styleable.InsetCompatLayout_iclType, 0)
        }
    }

    private fun withStyleable(styleId: IntArray, action: TypedArray.() -> Unit) {
        context.obtainStyledAttributes(attrs, styleId, defaultStyleAttr, defaultStyleRes).use {
            it.action()
        }
    }

    fun updateOriginalPadding(view: View) {
        paddingStart = view.paddingStart
        paddingTop = view.paddingTop
        paddingEnd = view.paddingEnd
        paddingBottom = view.paddingBottom
    }

    fun applyWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            assembleType(type)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                if (direction and DIRECTION_LEFT != 0 || direction and DIRECTION_START != 0) {
                    systemBars.left + paddingStart
                } else paddingStart,

                if (direction and DIRECTION_TOP != 0) {
                    systemBars.top + paddingTop
                } else paddingTop,
                if (direction and DIRECTION_RIGHT != 0 || direction and DIRECTION_END != 0) {
                    systemBars.right + paddingEnd
                } else paddingEnd,
                if (direction and DIRECTION_BOTTOM != 0) {
                    systemBars.bottom + paddingBottom
                } else paddingBottom
            )
            insets
        }
    }

    private fun assembleType(type: Int): Int {
        var assembledType = 0
        if (type and TYPE_STATUS_BAR != 0) {
            assembledType = type or WindowInsetsCompat.Type.statusBars()
        }
        if (type and TYPE_NAVIGATION_BAR != 0) {
            assembledType = type or WindowInsetsCompat.Type.navigationBars()
        }
        if (type and TYPE_CAPTION_BAR != 0) {
            assembledType = type or WindowInsetsCompat.Type.captionBar()
        }
        if (type and TYPE_GESTURE != 0) {
            assembledType = type or WindowInsetsCompat.Type.systemGestures()
        }
        if (type and TYPE_CUTOUT != 0) {
            assembledType = type or WindowInsetsCompat.Type.displayCutout()
        }
        return assembledType
    }

    companion object {
        const val DIRECTION_TOP = 0x01
        const val DIRECTION_BOTTOM = 0x02
        const val DIRECTION_LEFT = 0x04
        const val DIRECTION_RIGHT = 0x08
        const val DIRECTION_START = 0x10
        const val DIRECTION_END = 0x20

        const val TYPE_STATUS_BAR = 0x01
        const val TYPE_NAVIGATION_BAR = 0x02
        const val TYPE_CAPTION_BAR = 0x04
        const val TYPE_GESTURE = 0x08
        const val TYPE_CUTOUT = 0x10
    }

}