package com.app.base.ui.dialog.dsl.bottomsheet

import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.DialogConfig
import com.app.base.ui.dialog.dsl.DialogContextDslMarker
import com.app.base.ui.dialog.dsl.DisplayItem
import com.app.base.ui.dialog.dsl.DisplayList
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.OnItemClickListener
import com.app.base.ui.dialog.dsl.Text

@DialogContextDslMarker
interface ListBottomSheetDialogConfig : BottomSheetDialogConfig<BottomSheetDialogBehavior, ListBottomSheetDialogDescription> {

    fun title(text: CharSequence, config: Text.() -> Unit)
    fun title(@StringRes textRes: Int, config: Text.() -> Unit)

    fun decorateListTop(config: ListBottomSheetDialogInterface.(ConstraintLayout) -> Unit)
    fun decorateListBottom(config: ListBottomSheetDialogInterface.(ConstraintLayout) -> Unit)

    fun list(config: DisplayList.() -> Unit)

    fun items(list: List<DisplayItem>, onItemClickListener: OnItemClickListener? = null)

    fun customizeList(config: ListBottomSheetDialogInterface.(RecyclerView) -> Unit)

    fun bottomButton(text: CharSequence, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)

    fun bottomButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)

}

fun <T> ListBottomSheetDialogConfig.buildItems(
    list: List<T>,
    map: (T) -> DisplayItem,
    onItemClickListener: OnItemClickListener? = null,
) {
    items(list.map(map), onItemClickListener)
}

fun ListBottomSheetDialogConfig.textItems(
    list: List<String>,
    onItemClickListener: OnItemClickListener? = null,
) {
    buildItems(list, { DisplayItem(id = it, title = it, "") }, onItemClickListener)
}