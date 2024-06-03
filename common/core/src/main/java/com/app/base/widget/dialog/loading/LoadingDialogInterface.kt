package com.app.base.widget.dialog.loading

import com.app.base.widget.dialog.base.AppDialogInterface

interface LoadingDialogInterface : AppDialogInterface {

    fun updateMessage(message: CharSequence)

}