package com.app.base.ui.dialog.dsl.list

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.ButtonDescription
import com.app.base.ui.dialog.dsl.DialogBehaviorDescription
import com.app.base.ui.dialog.dsl.DialogDescription
import com.app.base.ui.dialog.dsl.DisplayListDescription
import com.app.base.ui.dialog.dsl.TextDescription
import com.app.base.ui.dialog.dsl.DialogWindowSizeDescription

class ListDialogDescription(
    val size: DialogWindowSizeDescription,
    val title: TextDescription?,
    val listDescription: DisplayListDescription?,
    val positiveButton: ButtonDescription?,
    val negativeButton: ButtonDescription?,
    val behavior: DialogBehaviorDescription,
    val listTopAreaConfig: (ListDialogInterface.(ConstraintLayout) -> Unit)?,
    val listBottomAreaConfig: (ListDialogInterface.(ConstraintLayout) -> Unit)?,
    val customizeList: (ListDialogInterface.(RecyclerView) -> Unit)?,
) : DialogDescription