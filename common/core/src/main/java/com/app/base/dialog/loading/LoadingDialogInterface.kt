package com.app.base.dialog.loading

import android.content.DialogInterface

interface LoadingDialogInterface : DialogInterface {

    fun updateMessage(message: CharSequence)

}