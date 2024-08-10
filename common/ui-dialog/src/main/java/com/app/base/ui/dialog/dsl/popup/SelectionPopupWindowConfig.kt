package com.app.base.ui.dialog.dsl.popup

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.app.base.ui.dialog.dsl.PopupWindowConfig
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.SelectionList
import com.app.base.ui.dialog.dsl.Text

interface SelectionPopupWindowConfig<Description : SelectionPopupWindowDescription> : PopupWindowConfig<Description> {

    fun title(text: CharSequence, config: Text.() -> Unit = {})
    fun title(@StringRes textRes: Int, config: Text.() -> Unit = {})

    fun list(config: SelectionList.() -> Unit)

    fun selections(list: List<Selection>)

}

fun SelectionPopupWindowConfig<*>.textSelections(list: List<String>) {
    selections(list.map { Selection(id = it, title = it, "") })
}

fun <T> SelectionPopupWindowConfig<*>.buildSelections(list: List<T>, map: (T) -> Selection) {
    selections(list.map(map))
}

fun SelectionPopupWindowConfig<*>.textSelections(vararg texts: String) {
    textSelections(texts.toList())
}

context(Context)
fun SelectionPopupWindowConfig<*>.resSelections(@StringRes vararg textResArr: Int) {
    textSelections(textResArr.map { getString(it) })
}

context(Context)
fun SelectionPopupWindowConfig<*>.arrResSelections(@ArrayRes textArrRes: Int) {
    textSelections(resources.getTextArray(textArrRes).map { it.toString() })
}

context(Fragment)
fun SelectionPopupWindowConfig<*>.resSelections(@StringRes vararg textResArr: Int) {
    textSelections(textResArr.map { getString(it) })
}

context(Fragment)
fun SelectionPopupWindowConfig<*>.arrResSelections(@ArrayRes textArrRes: Int) {
    textSelections(resources.getTextArray(textArrRes).map { it.toString() })
}

