package com.app.base.ui.dialog.dsl

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.InputFilter
import android.text.InputType
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.android.base.ui.shape.ShapeableEditText
import com.google.android.material.color.MaterialColors


@DialogContextDslMarker
class Field(
    context: Context,
    text: CharSequence = "",
) : Text(context, text) {

    private var _id = Condition.FIELD

    private var _hint: CharSequence = ""

    @ColorInt private var _hintTxtColor: Int = Color.GRAY

    private var _inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

    private var _maxLines = 1

    private var _lines = 1

    private val _filters = mutableListOf<InputFilter>()

    @ColorInt private var _backgroundColor: Int = Color.GRAY

    private var _cornerSize: Float = 0F

    fun id(id: Int) {
        _id = id
    }

    fun text(text: CharSequence) {
        _text = text
    }

    fun text(@StringRes textRes: Int) {
        _text = context.getText(textRes)
    }

    fun hint(hint: CharSequence) {
        _hint = hint
    }

    fun hint(@StringRes hintRes: Int) {
        _hint = context.getText(hintRes)
    }

    fun hintColor(@ColorInt color: Int) {
        _hintTxtColor = color
    }

    fun hintColorAttr(@AttrRes colorAttr: Int) {
        _hintTxtColor = MaterialColors.getColor(context, colorAttr, "hintColorAttr is not found in your theme.")
    }

    fun inputType(inputType: Int) {
        _inputType = inputType
    }

    fun maxLines(maxLines: Int) {
        _maxLines = maxLines
        if (maxLines < _lines) {
            _lines = maxLines
        }
    }

    fun lines(lines: Int) {
        _lines = lines
        if (lines > _maxLines) {
            _maxLines = lines
        }
    }

    fun maxLength(maxLength: Int) {
        _filters += InputFilter.LengthFilter(maxLength)
    }

    fun filters(vararg filter: InputFilter) {
        _filters += filter
    }

    fun backgroundColor(@ColorInt color: Int) {
        _backgroundColor = color
    }

    fun backgroundColorAttr(@AttrRes colorAttr: Int) {
        _backgroundColor = MaterialColors.getColor(context, colorAttr, "backgroundColorAttr is not found in your theme.")
    }

    fun cornerSize(cornerSize: Float) {
        _cornerSize = cornerSize
    }

    fun toFieldDescription(): FieldDescription {
        return FieldDescription(
            id = _id,
            textDescription = toTextDescription(),
            hint = _hint,
            hintTextColor = _hintTxtColor,
            inputType = _inputType,
            maxLines = _maxLines,
            lines = _lines,
            filters = _filters.toTypedArray(),
            backgroundColor = _backgroundColor,
            cornerSize = _cornerSize,
        )
    }

}

class FieldDescription(
    val id: Int,
    val textDescription: TextDescription,
    val hint: CharSequence,
    @ColorInt val hintTextColor: Int,
    val inputType: Int,
    val lines: Int,
    val maxLines: Int,
    val filters: Array<InputFilter>,
    @ColorInt val backgroundColor: Int,
    val cornerSize: Float,
)

fun FieldDescription.applyTo(editText: ShapeableEditText) {
    textDescription.applyTo(editText)
    editText.hint = hint
    editText.setHintTextColor(hintTextColor)
    editText.inputType = inputType
    editText.maxLines = maxLines
    editText.setLines(lines)
    editText.filters += filters
    editText.getShapeDrawable().apply {
        fillColor = ColorStateList.valueOf(backgroundColor)
        setCornerSize(cornerSize)
    }
    editText.setSelection(editText.length())
    editText.setConditionId(id)
}