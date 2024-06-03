package com.app.base.widget.dialog.confirm

import com.app.base.widget.dialog.base.AppDialogInterface

interface ConfirmDialogInterface : AppDialogInterface {

    fun updateMessage(message: CharSequence)

    var positiveEnable: Boolean

}