package com.app.base.ui.dialog.dsl

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

typealias OnOptionSelectedListener = DialogInterface.(Int, Option) -> Unit

data class Option(val title: CharSequence, @DrawableRes val icon: Int = 0)

class OptionList(
    internal val context: Context,
    private var _items: List<Option> = emptyList(),
    private var _itemStyle: TextStyle,
) {

    private var _onOptionSelectedListener: OnOptionSelectedListener? = null

    fun options(items: List<Option>) {
        _items = items
    }

    fun onOptionSelected(onOptionSelectedListener: OnOptionSelectedListener?) {
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

fun OptionList.options(vararg options: Option) {
    options(options.toList())
}

fun OptionList.textOptions(items: List<CharSequence>) {
    options(items.map { Option(it) })
}

fun OptionList.textOptions(vararg texts: CharSequence) {
    textOptions(texts.toList())
}

fun OptionList.resOptions(@StringRes vararg textResArr: Int) {
    textOptions(textResArr.map { context.getText(it) })
}

fun OptionList.arrResOptions(@ArrayRes textArrRes: Int) {
    textOptions(context.resources.getTextArray(textArrRes).toList())
}

class OptionListDescription(
    val items: List<Option>,
    val onOptionSelectedListener: OnOptionSelectedListener,
    val itemStyle: TextStyleDescription,
)