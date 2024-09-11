package com.app.base.ui.dialog.dsl.list

import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.DialogContextDslMarker
import com.app.base.ui.dialog.dsl.OnMultiSelectionSelectedListener

@DialogContextDslMarker
interface MultiSelectionListDialogConfig : SelectionListDialogConfig<MultiSelectionListDialogDescription> {

    fun decorateListTop(config: MultiSelectionListDialogInterface.(ConstraintLayout) -> Unit)

    fun decorateListBottom(config: MultiSelectionListDialogInterface.(ConstraintLayout) -> Unit)

    fun customizeList(config: MultiSelectionListDialogInterface.(RecyclerView) -> Unit)

    fun positiveButton(text: CharSequence, config: Button.() -> Unit = {}, onSelected: OnMultiSelectionSelectedListener)

    fun positiveButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onSelected: OnMultiSelectionSelectedListener)

}