package com.app.base.ui.dialog.dsl

import android.content.Context
import android.widget.CheckBox

@DialogContextDslMarker
class CheckBox(
    context: Context,
    text: CharSequence,
    private val isChecked: Boolean,
) : Text(context, text) {

    private var _id = Condition.CHECKBOX

    fun id(id: Int) {
        _id = id
    }

    fun toCheckBoxDescription(): CheckBoxDescription {
        return CheckBoxDescription(super.toTextDescription(), isChecked, _id)
    }

}

class CheckBoxDescription(
    val textDescription: TextDescription,
    val isChecked: Boolean,
    val id: Int,
)

fun CheckBoxDescription.applyTo(checkBox: CheckBox) {
    textDescription.applyTo(checkBox as android.widget.TextView)
    checkBox.isChecked = isChecked
    checkBox.setConditionId(id)
}