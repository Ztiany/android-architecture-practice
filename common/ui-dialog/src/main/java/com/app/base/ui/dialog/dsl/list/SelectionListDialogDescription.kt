package com.app.base.ui.dialog.dsl.list

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.ButtonDescription
import com.app.base.ui.dialog.dsl.DialogBehaviorDescription
import com.app.base.ui.dialog.dsl.DialogDescription
import com.app.base.ui.dialog.dsl.DialogWindowSizeDescription
import com.app.base.ui.dialog.dsl.OnMultiSelectionSelectedListener
import com.app.base.ui.dialog.dsl.OnSingleSelectionSelectedListener
import com.app.base.ui.dialog.dsl.SelectionListDescription
import com.app.base.ui.dialog.dsl.TextDescription
import com.app.base.ui.dialog.dsl.TextStyleDescription

sealed class SelectionListDialogDescription(
    val size: DialogWindowSizeDescription,
    val title: TextDescription?,
    val list: SelectionListDescription?,
    val behavior: DialogBehaviorDescription,
    val negativeButton: ButtonDescription?,
    val positiveButton: ButtonDescription?,
) : DialogDescription

class SingleSelectionListDialogDescription(
    size: DialogWindowSizeDescription,
    title: TextDescription?,
    list: SelectionListDescription?,
    behavior: DialogBehaviorDescription,
    negativeButton: ButtonDescription?,
    positiveButton: ButtonDescription?,
    val listTopAreaConfig: (SingleSelectionListDialogInterface.(ConstraintLayout) -> Unit)?,
    val listBottomAreaConfig: (SingleSelectionListDialogInterface.(ConstraintLayout) -> Unit)?,
    val customizeList: (SingleSelectionListDialogInterface.(RecyclerView) -> Unit)?,
    val onSingleItemSelectedListener: OnSingleSelectionSelectedListener? = null,
) : SelectionListDialogDescription(
    size,
    title,
    list,
    behavior,
    negativeButton,
    positiveButton,
)

class MultiSelectionListDialogDescription(
    size: DialogWindowSizeDescription,
    title: TextDescription?,
    list: SelectionListDescription?,
    behavior: DialogBehaviorDescription,
    negativeButton: ButtonDescription?,
    positiveButton: ButtonDescription?,
    val listTopAreaConfig: (MultiSelectionListDialogInterface.(ConstraintLayout) -> Unit)?,
    val listBottomAreaConfig: (MultiSelectionListDialogInterface.(ConstraintLayout) -> Unit)?,
    val customizeList: (MultiSelectionListDialogInterface.(RecyclerView) -> Unit)?,
    val rightTitleActionTextStyle: TextStyleDescription,
    val onMultiItemSelectedListener: OnMultiSelectionSelectedListener? = null,
) : SelectionListDialogDescription(
    size,
    title,
    list,
    behavior,
    negativeButton,
    positiveButton,
)

fun SelectionListDialogDescription.listCustomized(): Boolean {
    return when (this) {
        is SingleSelectionListDialogDescription -> this.customizeList != null
        is MultiSelectionListDialogDescription -> this.customizeList != null
    }
}