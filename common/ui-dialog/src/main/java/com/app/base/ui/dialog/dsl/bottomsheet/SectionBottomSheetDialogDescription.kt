package com.app.base.ui.dialog.dsl.bottomsheet

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.BottomSheetWindowSizeDescription
import com.app.base.ui.dialog.dsl.ButtonDescription
import com.app.base.ui.dialog.dsl.DialogDescription
import com.app.base.ui.dialog.dsl.OnMultiSelectionSelectedListener
import com.app.base.ui.dialog.dsl.OnSingleSelectionSelectedListener
import com.app.base.ui.dialog.dsl.SelectionListDescription
import com.app.base.ui.dialog.dsl.TextDescription
import com.app.base.ui.dialog.dsl.TextStyleDescription

sealed class SectionBottomSheetDialogDescription(
    val size: BottomSheetWindowSizeDescription,
    val title: TextDescription?,
    val list: SelectionListDescription?,
    val behavior: BottomSheetDialogBehaviorDescription,
    val positiveButton: ButtonDescription?,
) : DialogDescription

class SingleSectionBottomSheetDialogDescription(
    size: BottomSheetWindowSizeDescription,
    title: TextDescription?,
    list: SelectionListDescription?,
    behavior: BottomSheetDialogBehaviorDescription,
    positiveButton: ButtonDescription?,
    val listTopAreaConfig: (SingleSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)?,
    val listBottomAreaConfig: (SingleSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)?,
    val customizeList: (SingleSelectionBottomSheetDialogInterface.(RecyclerView) -> Unit)?,
    val onSingleItemSelectedListener: OnSingleSelectionSelectedListener? = null,
) : SectionBottomSheetDialogDescription(
    size,
    title,
    list,
    behavior,
    positiveButton,
)

class MultiSectionBottomSheetDialogDescription(
    size: BottomSheetWindowSizeDescription,
    title: TextDescription?,
    list: SelectionListDescription?,
    behavior: BottomSheetDialogBehaviorDescription,
    positiveButton: ButtonDescription?,
    val listTopAreaConfig: (MultiSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)?,
    val listBottomAreaConfig: (MultiSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)?,
    val customizeList: (MultiSelectionBottomSheetDialogInterface.(RecyclerView) -> Unit)?,
    val rightTitleActionTextStyle: TextStyleDescription,
    val onMultiItemSelectedListener: OnMultiSelectionSelectedListener? = null,
) : SectionBottomSheetDialogDescription(
    size,
    title,
    list,
    behavior,
    positiveButton,
)

fun SectionBottomSheetDialogDescription.listCustomized(): Boolean {
    return when (this) {
        is SingleSectionBottomSheetDialogDescription -> this.customizeList != null
        is MultiSectionBottomSheetDialogDescription -> this.customizeList != null
    }
}