package com.app.base.ui.dialog.dsl.list

import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.DialogBehavior
import com.app.base.ui.dialog.dsl.DialogConfig
import com.app.base.ui.dialog.dsl.DialogContextDslMarker
import com.app.base.ui.dialog.dsl.DisplayList
import com.app.base.ui.dialog.dsl.DisplayItem
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.OnItemClickListener
import com.app.base.ui.dialog.dsl.Text
import com.app.base.ui.dialog.dsl.DialogWindowSize

@DialogContextDslMarker
interface ListDialogConfig : DialogConfig<DialogBehavior, ListDialogDescription> {

    fun size(config: DialogWindowSize.() -> Unit)

    fun title(text: CharSequence, config: Text.() -> Unit = {})
    fun title(@StringRes textRes: Int, config: Text.() -> Unit = {})

    fun decorateListTop(config: ListDialogInterface.(ConstraintLayout) -> Unit)
    fun decorateListBottom(config: ListDialogInterface.(ConstraintLayout) -> Unit)

    fun list(config: DisplayList.() -> Unit)

    fun items(list: List<DisplayItem>, onItemClickListener: OnItemClickListener? = null)

    fun customizeList(config: ListDialogInterface.(RecyclerView) -> Unit)

    fun positiveButton(text: CharSequence, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)
    fun positiveButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)

    fun negativeButton(text: CharSequence, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)
    fun negativeButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)

}

fun <T> ListDialogConfig.buildItems(
    list: List<T>,
    map: (T) -> DisplayItem,
    onItemClickListener: OnItemClickListener? = null,
) {
    items(list.map(map), onItemClickListener)
}

fun ListDialogConfig.textItems(
    list: List<String>,
    onItemClickListener: OnItemClickListener? = null,
) {
    buildItems(list, { DisplayItem(id = it, title = it, "") }, onItemClickListener)
}