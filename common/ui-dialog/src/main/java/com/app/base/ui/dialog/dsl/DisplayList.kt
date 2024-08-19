package com.app.base.ui.dialog.dsl

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.app.base.ui.dialog.defaultDisplayListItemSubtitleStyle
import com.app.base.ui.dialog.defaultDisplayListItemTitleStyle
import com.google.android.material.color.MaterialColors

data class DisplayItem(
    val id: String,
    val title: CharSequence,
    val subtitle: CharSequence = "",
)

typealias OnItemClickListener = DialogInterface.(position: Int, item: DisplayItem) -> Unit

@DialogContextDslMarker
class DisplayList(
    internal val context: Context,
    private var _items: List<DisplayItem> = emptyList(),
    private var _itemClickListener: OnItemClickListener? = null,
    private var _titleStyle: TextStyle,
    private var _subtitleStyle: TextStyle,
    private var _dividerColor: Int,
) {

    /** unit: px */
    private var _dividerThickness: Int = 1

    private var _dividerInsetStart = 0
    private var _dividerInsetEnd = 0

    fun items(items: List<DisplayItem>) {
        _items = items
    }

    fun onItemClicked(itemClickListener: OnItemClickListener) {
        _itemClickListener = itemClickListener
    }

    fun divider(
        @ColorInt color: Int,
        /** unit: px */
        thickness: Int = 0,
    ) {
        _dividerColor = color
        _dividerThickness = thickness
    }

    fun dividerColor(@ColorInt color: Int) {
        _dividerColor = color
    }

    fun dividerColorAttr(@AttrRes colorAttr: Int) {
        _dividerColor = MaterialColors.getColor(context, colorAttr, "dividerColorAttr is not found in your theme.")
    }

    /** unit: px */
    fun dividerThickness(thickness: Int) {
        _dividerThickness = thickness
    }

    /** unit: px */
    fun dividerInsets(start: Int, end: Int) {
        _dividerInsetStart = start
        _dividerInsetEnd = end
    }

    fun titleStyle(config: TextStyle.() -> Unit) {
        _titleStyle = context.defaultDisplayListItemTitleStyle().apply(config)
    }

    fun subtitleStyle(config: TextStyle.() -> Unit) {
        _subtitleStyle = context.defaultDisplayListItemSubtitleStyle().apply(config)
    }

    fun toDisplayListDescription(): DisplayListDescription {
        return DisplayListDescription(
            items = _items,
            itemClickListener = _itemClickListener,
            titleStyle = _titleStyle.toTextStyleDescription(),
            subtitleStyle = _subtitleStyle.toTextStyleDescription(),
            dividerColor = _dividerColor,
            dividerThickness = _dividerThickness,
            dividerInsetStart = _dividerInsetStart,
            dividerInsetEnd = _dividerInsetEnd,
        )
    }

}

fun <T> DisplayList.buildItems(
    list: List<T>,
    map: (T) -> DisplayItem,
) {
    items(list.map(map))
}

fun DisplayList.textItems(list: List<String>) {
    buildItems(list) { DisplayItem(id = it, title = it, "") }
}

fun DisplayList.textItems(vararg texts: String) {
    textItems(texts.toList())
}

fun DisplayList.resItems(@StringRes vararg textResArr: Int) {
    textItems(textResArr.map { context.getString(it) })
}

fun DisplayList.arrResItems(@ArrayRes textArrRes: Int) {
    textItems(context.resources.getTextArray(textArrRes).map { it.toString() })
}

class DisplayListDescription(
    val items: List<DisplayItem>,
    val itemClickListener: OnItemClickListener?,
    val titleStyle: TextStyleDescription,
    val subtitleStyle: TextStyleDescription,
    val dividerColor: Int,
    val dividerThickness: Int,
    val dividerInsetStart: Int,
    val dividerInsetEnd: Int,
)
