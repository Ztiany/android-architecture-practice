package com.app.base.ui.widget.insets

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.use
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.base.utils.common.unsafeLazy
import com.app.base.ui.widget.R

class InsetsCompatHelper(
    private val context: Context,
    private val attrs: AttributeSet?,
    private val defaultStyleAttr: Int = 0,
    private val defaultStyleRes: Int = 0,
) {

    private var direction = 0
    private var type = 0

    private var paddingLeft = 0
    private var paddingTop = 0
    private var paddingRight = 0
    private var paddingBottom = 0

    private var leftInsetColor = Color.TRANSPARENT
    private var topInsetColor = Color.TRANSPARENT
    private var rightInsetColor = Color.TRANSPARENT
    private var bottomInsetColor = Color.TRANSPARENT

    private val insetsSize = Rect()

    private val insetPaint by unsafeLazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply { strokeWidth = 1F }
    }

    init {
        withStyleable(R.styleable.InsetCompatLayout) {
            direction = getInt(R.styleable.InsetCompatLayout_iclDirection, 0)
            type = getInt(R.styleable.InsetCompatLayout_iclType, 0)
            leftInsetColor = getColor(R.styleable.InsetCompatLayout_iclLeftInsetColor, Color.TRANSPARENT)
            topInsetColor = getColor(R.styleable.InsetCompatLayout_iclTopInsetColor, Color.TRANSPARENT)
            rightInsetColor = getColor(R.styleable.InsetCompatLayout_iclRightInsetColor, Color.TRANSPARENT)
            bottomInsetColor = getColor(R.styleable.InsetCompatLayout_iclBottomInsetColor, Color.TRANSPARENT)
        }
    }

    private fun withStyleable(styleId: IntArray, action: TypedArray.() -> Unit) {
        context.obtainStyledAttributes(attrs, styleId, defaultStyleAttr, defaultStyleRes).use {
            it.action()
        }
    }

    fun updateOriginalPadding(view: View) {
        paddingLeft = view.paddingStart
        paddingTop = view.paddingTop
        paddingRight = view.paddingEnd
        paddingBottom = view.paddingBottom
    }

    fun applyWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            assembleType(type)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            insetsSize.set(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            view.setPadding(
                if (direction and DIRECTION_LEFT != 0) {
                    systemBars.left
                } else paddingLeft,

                if (direction and DIRECTION_TOP != 0) {
                    systemBars.top
                } else paddingTop,
                if (direction and DIRECTION_RIGHT != 0) {
                    systemBars.right
                } else paddingRight,
                if (direction and DIRECTION_BOTTOM != 0) {
                    systemBars.bottom
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

    fun drawableInsetsColor(canvas: Canvas, target: View) {
        if (direction and DIRECTION_LEFT != 0) {
            insetPaint.setColor(leftInsetColor)
            canvas.drawRect(0F, 0F, insetsSize.left.toFloat(), target.height.toFloat(), insetPaint)
        }
        if (direction and DIRECTION_TOP != 0) {
            insetPaint.setColor(topInsetColor)
            canvas.drawRect(0F, 0F, target.width.toFloat(), insetsSize.top.toFloat(), insetPaint)
        }
        if (direction and DIRECTION_RIGHT != 0) {
            insetPaint.setColor(rightInsetColor)
            canvas.drawRect(target.width.toFloat() - insetsSize.right, 0F, target.width.toFloat(), target.height.toFloat(), insetPaint)
        }
        if (direction and DIRECTION_BOTTOM != 0) {
            insetPaint.setColor(bottomInsetColor)
            canvas.drawRect(0F, target.height.toFloat() - insetsSize.bottom, target.width.toFloat(), target.height.toFloat(), insetPaint)
        }
    }

    companion object {
        const val DIRECTION_TOP = 0x01
        const val DIRECTION_BOTTOM = 0x02
        const val DIRECTION_LEFT = 0x04
        const val DIRECTION_RIGHT = 0x08

        const val TYPE_STATUS_BAR = 0x01
        const val TYPE_NAVIGATION_BAR = 0x02
        const val TYPE_CAPTION_BAR = 0x04
        const val TYPE_GESTURE = 0x08
        const val TYPE_CUTOUT = 0x10
    }

}