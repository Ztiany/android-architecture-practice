package com.app.base.ui.dialog.dsl.alert


import androidx.constraintlayout.widget.ConstraintLayout
import com.app.base.ui.dialog.dsl.ButtonDescription
import com.app.base.ui.dialog.dsl.CheckBoxDescription
import com.app.base.ui.dialog.dsl.DialogBehaviorDescription
import com.app.base.ui.dialog.dsl.DialogDescription
import com.app.base.ui.dialog.dsl.DialogWindowSizeDescription
import com.app.base.ui.dialog.dsl.TextDescription

class AlertDialogDescription(
    val size: DialogWindowSizeDescription,
    val title: TextDescription? = null,
    val message: TextDescription?,
    val messageConfig: ((AlertDialogInterface, ConstraintLayout) -> Unit)?,
    val checkBox: CheckBoxDescription? = null,
    val positiveButton: ButtonDescription? = null,
    val neutralButton: ButtonDescription? = null,
    val negativeButton: ButtonDescription? = null,
    val behavior: DialogBehaviorDescription,
) : DialogDescription