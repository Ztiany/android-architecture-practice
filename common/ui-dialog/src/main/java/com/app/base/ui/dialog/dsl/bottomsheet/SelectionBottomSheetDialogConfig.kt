package com.app.base.ui.dialog.dsl.bottomsheet

import androidx.annotation.StringRes
import com.app.base.ui.dialog.dsl.DialogConfig
import com.app.base.ui.dialog.dsl.DialogContextDslMarker
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.SelectionList
import com.app.base.ui.dialog.dsl.Text

@DialogContextDslMarker
interface SelectionBottomSheetDialogConfig<Description : SectionBottomSheetDialogDescription> : BottomSheetDialogConfig<BottomSheetDialogBehavior, Description> {

    fun title(text: CharSequence, config: Text.() -> Unit = {})
    fun title(@StringRes textRes: Int, config: Text.() -> Unit = {})

    fun list(config: SelectionList.() -> Unit)

    fun selections(list: List<Selection>)

}

fun SelectionBottomSheetDialogConfig<*>.textSelections(list: List<String>) {
    selections(list.map { Selection(id = it, title = it, "") })
}

fun <T> SelectionBottomSheetDialogConfig<*>.buildSelections(list: List<T>, map: (T) -> Selection) {
    selections(list.map(map))
}
