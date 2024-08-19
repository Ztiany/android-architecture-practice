package com.app.base.ui.dialog.dsl.bottomsheet

import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.DialogContextDslMarker
import com.app.base.ui.dialog.dsl.OnMultiSelectionSelectedListener

@DialogContextDslMarker
interface MultiSelectionBottomSheetDialogConfig : SelectionBottomSheetDialogConfig<MultiSectionBottomSheetDialogDescription> {

    fun decorateListTop(config: MultiSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)

    fun decorateListBottom(config: MultiSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)

    fun customizeList(config: MultiSelectionBottomSheetDialogInterface.(RecyclerView) -> Unit)

    fun positiveButton(text: CharSequence, config: Button.() -> Unit = {}, onSelected: OnMultiSelectionSelectedListener)

    fun positiveButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onSelected: OnMultiSelectionSelectedListener)

}