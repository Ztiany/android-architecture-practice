package com.app.base.ui.dialog.dsl

import android.content.Context
import android.content.DialogInterface

typealias OnOptionSelectedListener = DialogInterface.(Int, CharSequence) -> Unit

class OptionList(
    private val context: Context,
    private var _items: List<CharSequence> = emptyList(),
    private var _itemStyle: TextStyle,
) {

    private var _onOptionSelectedListener: OnOptionSelectedListener? = null

    fun items(items: List<CharSequence>) {
        _items = items
    }

    fun onItemClick(onOptionSelectedListener: OnOptionSelectedListener?) {
        _onOptionSelectedListener = onOptionSelectedListener
    }

    fun itemStyle(config: TextStyle.() -> Unit) {
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

class OptionListDescription(
    val items: List<CharSequence>,
    val onOptionSelectedListener: OnOptionSelectedListener,
    val itemStyle: TextStyleDescription,
)