package com.app.base.ui.dialog.dsl

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

typealias OnOptionSelectedListener = DialogInterface.(Int, CharSequence) -> Unit

class OptionList(
    internal val context: Context,
    private var _items: List<CharSequence> = emptyList(),
    private var _itemStyle: TextStyle,
) {

    private var _onOptionSelectedListener: OnOptionSelectedListener? = null

    fun options(items: List<CharSequence>) {
        _items = items
    }

    fun onOptionClick(onOptionSelectedListener: OnOptionSelectedListener?) {
        _onOptionSelectedListener = onOptionSelectedListener
    }

    fun optionStyle(config: TextStyle.() -> Unit) {
        _itemStyle = TextStyle(context).apply(config)
    }

    fun toOptionListDescription(): OptionListDescription {
        return OptionListDescription(
            items = _items,
            onOptionSelectedListener = _onOptionSelectedListener ?: { _, _ -> },
            itemStyle = _itemStyle.toTextStyleDescription(),
        )
    }

}

fun OptionList.options(vararg texts: CharSequence) {
    options(texts.toList())
}

fun OptionList.resOptions(@StringRes vararg textResArr: Int) {
    options(textResArr.map { context.getText(it) })
}

fun OptionList.arrResOptions(@ArrayRes textArrRes: Int) {
    options(context.resources.getTextArray(textArrRes).toList())
}

class OptionListDescription(
    val items: List<CharSequence>,
    val onOptionSelectedListener: OnOptionSelectedListener,
    val itemStyle: TextStyleDescription,
)