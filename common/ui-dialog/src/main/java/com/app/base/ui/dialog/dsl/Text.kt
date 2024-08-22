package com.app.base.ui.dialog.dsl

import android.content.Context
import android.widget.TextView
import com.google.android.material.appbar.MaterialToolbar

@DialogContextDslMarker
open class Text(
    protected val context: Context,
    protected var _text: CharSequence,
) : TextStyle(context) {

    fun toTextDescription(): TextDescription {
        return TextDescription(
            text = _text,
            textStyle = toTextStyleDescription()
        )
    }

}

class TextDescription(
    val text: CharSequence,
    val textStyle: TextStyleDescription,
)

fun TextDescription.applyTo(textView: TextView) {
    textStyle.applyTo(textView)
    textView.text = text
}

fun TextDescription.applyTo(toolbar: MaterialToolbar) {
    toolbar.setTitle(text.toString())
    toolbar.setTitleTextColor(textStyle.textColor)
}