package com.app.base.ui.dialog.dsl.popup

import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.OnMultiItemSelectedListener

interface MultiSelectionPopupWindowConfig : SelectionPopupWindowConfig<MultiSelectionPopupWindowDescription> {

    fun decorateListTop(config: MultiSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)

    fun decorateListBottom(config: MultiSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)

    fun customizeList(config: MultiSelectionPopupWindowInterface.(RecyclerView) -> Unit)

    fun positiveButton(text: CharSequence, config: Button.() -> Unit = {}, onSelected: OnMultiItemSelectedListener)

    fun positiveButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onSelected: OnMultiItemSelectedListener)

}