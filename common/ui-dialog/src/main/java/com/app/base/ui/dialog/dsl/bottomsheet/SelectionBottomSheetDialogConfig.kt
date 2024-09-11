package com.app.base.ui.dialog.dsl.bottomsheet

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.app.base.ui.dialog.dsl.DialogContextDslMarker
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.SelectionList
import com.app.base.ui.dialog.dsl.Text

@DialogContextDslMarker
interface SelectionBottomSheetDialogConfig<Description : SelectionBottomSheetDialogDescription> :
    BottomSheetDialogConfig<BottomSheetDialogBehavior, Description> {

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

fun SelectionBottomSheetDialogConfig<*>.textSelections(vararg texts: String) {
    textSelections(texts.toList())
}

context(Context)
fun SelectionBottomSheetDialogConfig<*>.resSelections(@StringRes vararg textResArr: Int) {
    textSelections(textResArr.map { getString(it) })
}

context(Context)
fun SelectionBottomSheetDialogConfig<*>.arrResSelections(@ArrayRes textArrRes: Int) {
    textSelections(resources.getTextArray(textArrRes).map { it.toString() })
}

context(Fragment)
fun SelectionBottomSheetDialogConfig<*>.resSelections(@StringRes vararg textResArr: Int) {
    textSelections(textResArr.map { getString(it) })
}

context(Fragment)
fun SelectionBottomSheetDialogConfig<*>.arrResSelections(@ArrayRes textArrRes: Int) {
    textSelections(resources.getTextArray(textArrRes).map { it.toString() })
}