package com.app.base.ui.dialog.impl

import android.content.DialogInterface

internal class DialogInterfaceWrapper(private val dialogInterface: DialogInterface) : DialogInterface by dialogInterface {

    var canceledByUser = false
        private set

    override fun dismiss() {
        canceledByUser = true
        dialogInterface.dismiss()
    }

}