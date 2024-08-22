package com.app.base.ui.dialog.dsl.input

import android.content.DialogInterface

interface InputDialogInterface : DialogInterface {

    var isPositiveButtonEnable: Boolean

    fun updateTitle(title: CharSequence)

    override fun dismiss()

    override fun cancel()

    fun show()

}