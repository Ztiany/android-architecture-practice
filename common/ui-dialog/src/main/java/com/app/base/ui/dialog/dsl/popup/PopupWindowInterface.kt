package com.app.base.ui.dialog.dsl.popup

import android.content.DialogInterface

interface PopupWindowInterface : DialogInterface {

    override fun cancel()

    override fun dismiss()

}
