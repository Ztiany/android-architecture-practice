package com.app.base.ui.dialog.dsl.popup

import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.OnSingleSelectionSelectedListener

interface SingleSelectionPopupWindowConfig : SelectionPopupWindowConfig<SingleSelectionPopupWindowDescription> {

    fun decorateListTop(config: SingleSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)

    fun decorateListBottom(config: SingleSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)

    fun customizeList(config: SingleSelectionPopupWindowInterface.(RecyclerView) -> Unit)

    fun positiveButton(text: CharSequence, config: Button.() -> Unit = {}, onSelected: OnSingleSelectionSelectedListener)

    fun positiveButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onSelected: OnSingleSelectionSelectedListener)

}