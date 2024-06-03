package com.app.base.widget.dialog

interface ConfirmDialogInterface : AppDialogInterface {

    fun updateMessage(message: CharSequence)

    var positiveEnable: Boolean

}