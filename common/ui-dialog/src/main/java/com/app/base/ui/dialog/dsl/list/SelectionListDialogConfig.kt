package com.app.base.ui.dialog.dsl.list

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.DialogBehavior
import com.app.base.ui.dialog.dsl.DialogConfig
import com.app.base.ui.dialog.dsl.DialogContextDslMarker
import com.app.base.ui.dialog.dsl.DialogWindowSize
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.SelectionList
import com.app.base.ui.dialog.dsl.Text

@DialogContextDslMarker
interface SelectionListDialogConfig<Description : SelectionListDialogDescription> :
    DialogConfig<DialogBehavior, Description> {

    fun size(config: DialogWindowSize.() -> Unit)

    fun title(text: CharSequence, config: Text.() -> Unit = {})
    fun title(@StringRes textRes: Int, config: Text.() -> Unit = {})

    fun list(config: SelectionList.() -> Unit)

    fun selections(list: List<Selection>)

    fun negativeButton(text: CharSequence, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)
    fun negativeButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)
}

fun SelectionListDialogConfig<*>.textSelections(list: List<String>) {
    selections(list.map { Selection(id = it, title = it, "") })
}

fun <T> SelectionListDialogConfig<*>.buildSelections(list: List<T>, map: (T) -> Selection) {
    selections(list.map(map))
}

fun SelectionListDialogConfig<*>.textSelections(vararg texts: String) {
    textSelections(texts.toList())
}

context(Context)
fun SelectionListDialogConfig<*>.resSelections(@StringRes vararg textResArr: Int) {
    textSelections(textResArr.map { getString(it) })
}

context(Context)
fun SelectionListDialogConfig<*>.arrResSelections(@ArrayRes textArrRes: Int) {
    textSelections(resources.getTextArray(textArrRes).map { it.toString() })
}

context(Fragment)
fun SelectionListDialogConfig<*>.resSelections(@StringRes vararg textResArr: Int) {
    textSelections(textResArr.map { getString(it) })
}

context(Fragment)
fun SelectionListDialogConfig<*>.arrResSelections(@ArrayRes textArrRes: Int) {
    textSelections(resources.getTextArray(textArrRes).map { it.toString() })
}