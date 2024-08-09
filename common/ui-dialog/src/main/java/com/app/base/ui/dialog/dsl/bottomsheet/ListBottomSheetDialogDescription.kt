package com.app.base.ui.dialog.dsl.bottomsheet

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.BottomSheetWindowSizeDescription
import com.app.base.ui.dialog.dsl.ButtonDescription
import com.app.base.ui.dialog.dsl.DialogDescription
import com.app.base.ui.dialog.dsl.DisplayListDescription
import com.app.base.ui.dialog.dsl.TextDescription

class ListBottomSheetDialogDescription(
    val size: BottomSheetWindowSizeDescription,
    val title: TextDescription?,
    val listTopAreaConfig: (ListBottomSheetDialogInterface.(ConstraintLayout) -> Unit)?,
    val listBottomAreaConfig: (ListBottomSheetDialogInterface.(ConstraintLayout) -> Unit)?,
    val customizeList: (ListBottomSheetDialogInterface.(RecyclerView) -> Unit)?,
    val listDescription: DisplayListDescription?,
    val behavior: BottomSheetDialogBehaviorDescription,
    val bottomButton: ButtonDescription?,
) : DialogDescription