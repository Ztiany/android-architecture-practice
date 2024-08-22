package com.app.base.ui.dialog.dsl.input


import androidx.constraintlayout.widget.ConstraintLayout
import com.app.base.ui.dialog.dsl.ButtonDescription
import com.app.base.ui.dialog.dsl.DialogDescription
import com.app.base.ui.dialog.dsl.DialogWindowSizeDescription
import com.app.base.ui.dialog.dsl.FieldDescription
import com.app.base.ui.dialog.dsl.TextDescription

class InputDialogDescription(
    val size: DialogWindowSizeDescription,
    val title: TextDescription? = null,
    val filed: FieldDescription?,
    val positiveButton: ButtonDescription? = null,
    val negativeButton: ButtonDescription? = null,
    val fieldTopAreaConfig: (InputDialogInterface.(ConstraintLayout) -> Unit)?,
    val fieldBottomAreaConfig: (InputDialogInterface.(ConstraintLayout) -> Unit)?,
    val behavior: InputDialogBehaviorDescription,
) : DialogDescription