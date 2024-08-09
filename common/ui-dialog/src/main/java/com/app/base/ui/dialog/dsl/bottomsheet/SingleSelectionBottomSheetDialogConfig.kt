package com.app.base.ui.dialog.dsl.bottomsheet

import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.OnSingleItemSelectedListener

interface SingleSelectionBottomSheetDialogConfig : SelectionBottomSheetDialogConfig<SingleSectionBottomSheetDialogDescription> {

    fun decorateListTop(config: SingleSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)

    fun decorateListBottom(config: SingleSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)

    fun customizeList(config: SingleSelectionBottomSheetDialogInterface.(RecyclerView) -> Unit)

    fun positiveButton(text: CharSequence, config: Button.() -> Unit = {}, onSelected: OnSingleItemSelectedListener)

    fun positiveButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onSelected: OnSingleItemSelectedListener)

}

