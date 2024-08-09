package com.app.base.ui.dialog.dsl.popup

import androidx.annotation.StringRes
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
