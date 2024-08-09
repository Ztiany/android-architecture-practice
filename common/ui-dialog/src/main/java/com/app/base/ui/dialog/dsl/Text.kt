package com.app.base.ui.dialog.dsl

import android.content.Context
import android.widget.TextView
import androidx.annotation.ColorInt
import com.google.android.material.appbar.MaterialToolbar

@DialogContextDslMarker
open class Text(
    protected val context: Context,
    private val _text: CharSequence,
) : TextStyle(context) {

    fun toTextDescription(): TextDescription {
        val styleDescription = toTextStyleDescription()
        return TextDescription(
            text = _text,
            textColor = styleDescription.textColor,
            textSize = styleDescription.textSize,
            textGravity = styleDescription.textGravity,
            clickableSpan = styleDescription.clickableSpan,
            isBold = styleDescription.isBold,
        )
    }

}

class TextDescription(
    val text: CharSequence,
    @ColorInt val textColor: Int,
    val textGravity: Int,
    val clickableSpan: Boolean,
    /** unit: sp */
    val textSize: Float,
    val isBold: Boolean,
)

fun TextDescription.applyTo(textView: TextView) {
    textView.setTextColor(textColor)
    textView.textSize = textSize
    textView.gravity = textGravity
    if (clickableSpan) {
        textView.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }
    if (isBold) {
        textView.paint.isFakeBoldText = true
    }
    textView.text = text
}

fun TextDescription.applyTo(toolbar: MaterialToolbar) {
    toolbar.setTitle(text.toString())
    toolbar.setTitleTextColor(textColor)
}