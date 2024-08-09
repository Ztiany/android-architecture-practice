package com.app.base.ui.dialog.dsl.alert

import android.content.DialogInterface

interface AlertDialogInterface : DialogInterface {

    var isPositiveButtonEnable: Boolean

    fun updateTitle(title: CharSequence)

    fun updateMessage(message: CharSequence)

    override fun dismiss()

    override fun cancel()

    fun show()

}