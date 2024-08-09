package com.app.base.ui.dialog.dsl

import android.content.Context
import android.content.DialogInterface

typealias OnClickListener = DialogInterface.(condition: Condition) -> Unit

@DialogContextDslMarker
class Button(
    context: Context,
    text: CharSequence,
) : Text(context, text) {

    private var _onClickListener: OnClickListener? = null

    fun onClick(listener: OnClickListener?) {
        _onClickListener = listener
    }

    fun toButtonDescription(): ButtonDescription {
        return ButtonDescription(super.toTextDescription(), _onClickListener)
    }

}

class ButtonDescription(
    val textDescription: TextDescription,
    val onClickListener: OnClickListener?,
)