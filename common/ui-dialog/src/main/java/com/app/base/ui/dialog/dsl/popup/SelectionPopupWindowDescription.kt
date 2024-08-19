package com.app.base.ui.dialog.dsl.popup

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.ButtonDescription
import com.app.base.ui.dialog.dsl.OnMultiSelectionSelectedListener
import com.app.base.ui.dialog.dsl.OnSingleSelectionSelectedListener
import com.app.base.ui.dialog.dsl.PopupDimDescription
import com.app.base.ui.dialog.dsl.PopupWindowBehaviorDescription
import com.app.base.ui.dialog.dsl.PopupWindowDescription
import com.app.base.ui.dialog.dsl.PopupWindowSizeDescription
import com.app.base.ui.dialog.dsl.SelectionListDescription
import com.app.base.ui.dialog.dsl.TextDescription
import com.app.base.ui.dialog.dsl.TextStyleDescription

sealed class SelectionPopupWindowDescription(
    val behavior: PopupWindowBehaviorDescription,
    val size: PopupWindowSizeDescription,
    val popupDim: PopupDimDescription?,
    val title: TextDescription?,
    val list: SelectionListDescription?,
    val positiveButton: ButtonDescription?,
) : PopupWindowDescription

class SingleSelectionPopupWindowDescription(
    behavior: PopupWindowBehaviorDescription,
    size: PopupWindowSizeDescription,
    popupDim: PopupDimDescription?,
    title: TextDescription?,
    list: SelectionListDescription?,
    bottomButton: ButtonDescription?,
    val listTopAreaConfig: (SingleSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)?,
    val listBottomAreaConfig: (SingleSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)?,
    val customizeList: (SingleSelectionPopupWindowInterface.(RecyclerView) -> Unit)?,
    val onSingleItemSelectedListener: OnSingleSelectionSelectedListener?,
) : SelectionPopupWindowDescription(
    behavior,
    size,
    popupDim,
    title,
    list,
    bottomButton,
)

class MultiSelectionPopupWindowDescription(
    behavior: PopupWindowBehaviorDescription,
    size: PopupWindowSizeDescription,
    popupDim: PopupDimDescription?,
    title: TextDescription?,
    list: SelectionListDescription?,
    bottomButton: ButtonDescription?,
    val listTopAreaConfig: (MultiSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)?,
    val listBottomAreaConfig: (MultiSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)?,
    val customizeList: (MultiSelectionPopupWindowInterface.(RecyclerView) -> Unit)?,
    val rightTitleActionTextStyle: TextStyleDescription,
    val onMultiItemSelectedListener: OnMultiSelectionSelectedListener?,
) : SelectionPopupWindowDescription(
    behavior,
    size,
    popupDim,
    title,
    list,
    bottomButton,
)

fun SelectionPopupWindowDescription.listCustomized(): Boolean {
    return when (this) {
        is SingleSelectionPopupWindowDescription -> this.customizeList != null
        is MultiSelectionPopupWindowDescription -> this.customizeList != null
    }
}