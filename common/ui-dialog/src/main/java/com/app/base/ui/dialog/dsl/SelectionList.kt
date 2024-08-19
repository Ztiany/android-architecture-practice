package com.app.base.ui.dialog.dsl

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.app.base.ui.dialog.defaultSelectionListItemSubtitleStyle
import com.app.base.ui.dialog.defaultSelectionListItemTitleStyle
import com.google.android.material.color.MaterialColors

data class Selection(
    val id: String,
    val title: CharSequence,
    val subtitle: CharSequence = "",
    val selected: Boolean = false,
)

typealias OnSingleSelectionSelectedListener = (position: Int, item: Selection) -> Unit

typealias OnMultiSelectionSelectedListener = (selected: List<Selection>) -> Unit

typealias OnSelectionClickedListener = DialogInterface.(position: Int, item: Selection) -> Unit

@DialogContextDslMarker
class SelectionList(
    internal val context: Context,
    private var _items: List<Selection> = emptyList(),
    private var _titleStyle: TextStyle,
    private var _subtitleStyle: TextStyle,
    private var _dividerColor: Int,
) {

    /** unit: px */
    private var _dividerThickness: Int = 1

    private var _dividerInsetStart = 0
    private var _dividerInsetEnd = 0

    private var _onSelectionClickListener: OnSelectionClickedListener? = null

    fun selections(items: List<Selection>) {
        _items = items
    }

    fun onSelectionClicked(onSelectionClickListener: OnSelectionClickedListener) {
        _onSelectionClickListener = onSelectionClickListener
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

    fun dividerInsets(start: Int, end: Int) {
        _dividerInsetStart = start
        _dividerInsetEnd = end
    }

    fun titleStyle(config: TextStyle.() -> Unit) {
        _titleStyle = context.defaultSelectionListItemTitleStyle().apply(config)
    }

    fun subtitleStyle(config: TextStyle.() -> Unit) {
        _subtitleStyle = context.defaultSelectionListItemSubtitleStyle().apply(config)
    }

    fun toSelectionListDescription(): SelectionListDescription {
        return SelectionListDescription(
            items = _items,
            onSelectionClickListener = _onSelectionClickListener,
            titleStyle = _titleStyle.toTextStyleDescription(),
            subtitleStyle = _subtitleStyle.toTextStyleDescription(),
            dividerColor = _dividerColor,
            dividerThickness = _dividerThickness,
            dividerInsetStart = _dividerInsetStart,
            dividerInsetEnd = _dividerInsetEnd,
        )
    }

}

fun SelectionList.textSelections(list: List<String>) {
    selections(list.map { Selection(id = it, title = it, "") })
}

fun <T> SelectionList.buildSelections(list: List<T>, map: (T) -> Selection) {
    selections(list.map(map))
}

fun SelectionList.textSelections(vararg texts: String) {
    textSelections(texts.toList())
}

fun SelectionList.resSelections(@StringRes vararg textResArr: Int) {
    textSelections(textResArr.map { context.getString(it) })
}

fun SelectionList.arrResSelections(@ArrayRes textArrRes: Int) {
    textSelections(context.resources.getTextArray(textArrRes).map { it.toString() })
}

class SelectionListDescription(
    val items: List<Selection>,
    val onSelectionClickListener: OnSelectionClickedListener?,
    val titleStyle: TextStyleDescription,
    val subtitleStyle: TextStyleDescription,
    val dividerColor: Int,
    val dividerThickness: Int,
    val dividerInsetStart: Int,
    val dividerInsetEnd: Int,
)