package com.app.base.ui.dialog.dsl

import android.content.Context
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.GravityInt
import com.google.android.material.color.MaterialColors

@DialogContextDslMarker
open class TextStyle(private val context: Context) {

    private var _textColor: Int = Color.BLACK

    private var _textSize: Float = 14F

    private var _textGravity: Int = android.view.Gravity.CENTER

    private var _clickableSpan: Boolean = false

    private var _isBold: Boolean = false

    fun textColor(@ColorInt textColor: Int) {
        _textColor = textColor
    }

    fun textColorAttr(@AttrRes textColorAttr: Int) {
        _textColor = MaterialColors.getColor(context, textColorAttr, "textColorAttr is not found in your theme.")
    }

    fun gravity(@GravityInt gravity: Int) {
        _textGravity = gravity
    }

    /** unit: sp */
    fun textSize(textSize: Float) {
        _textSize = textSize
    }

    fun asClickableSpan() {
        _clickableSpan = true
    }

    fun boldText() {
        _isBold = true
    }

    fun toTextStyleDescription(): TextStyleDescription {
        return TextStyleDescription(
            textColor = _textColor,
            textSize = _textSize,
            textGravity = _textGravity,
            clickableSpan = _clickableSpan,
            isBold = _isBold,
        )
    }

}

class TextStyleDescription(
    val textColor: Int,
    val textSize: Float,
    val textGravity: Int,
    val clickableSpan: Boolean,
    val isBold: Boolean,
)

fun TextStyleDescription.applyTo(textView: TextView) {
    textView.setTextColor(textColor)
    textView.textSize = textSize
    textView.gravity = textGravity
    if (clickableSpan) {
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
    if (isBold) {
        textView.paint.isFakeBoldText = true
    }
}