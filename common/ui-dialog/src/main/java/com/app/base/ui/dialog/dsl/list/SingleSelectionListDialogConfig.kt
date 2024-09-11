package com.app.base.ui.dialog.dsl.list

import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.OnSingleSelectionSelectedListener

interface SingleSelectionListDialogConfig : SelectionListDialogConfig<SingleSelectionListDialogDescription> {

    fun decorateListTop(config: SingleSelectionListDialogInterface.(ConstraintLayout) -> Unit)

    fun decorateListBottom(config: SingleSelectionListDialogInterface.(ConstraintLayout) -> Unit)

    fun customizeList(config: SingleSelectionListDialogInterface.(RecyclerView) -> Unit)

    fun positiveButton(text: CharSequence, config: Button.() -> Unit = {}, onSelected: OnSingleSelectionSelectedListener)

    fun positiveButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onSelected: OnSingleSelectionSelectedListener)

}