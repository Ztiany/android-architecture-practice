package com.app.base.widget.dialog.base

import android.app.Dialog
import android.content.DialogInterface
import androidx.fragment.app.FragmentActivity
import com.android.base.utils.android.views.onDismiss

interface AppDialogInterface {

    fun dismiss()

    val dialog: Dialog

}

fun AppDialogInterface.setOnDismissListener(listener: DialogInterface.OnDismissListener) {
    dialog.setOnDismissListener(listener)
}

fun AppDialogInterface.onDismiss(action: () -> Unit): AppDialogInterface {
    dialog.onDismiss(action)
    return this
}

fun AppDialogInterface.setCancelable(cancelable: Boolean): AppDialogInterface {
    dialog.setCancelable(cancelable)
    return this
}

fun AppDialogInterface.setCanceledOnTouchOutside(cancelable: Boolean): AppDialogInterface {
    dialog.setCanceledOnTouchOutside(cancelable)
    return this
}

val AppDialogInterface.activityContext: FragmentActivity?
    get() {
        var context = dialog.context
        while (context is android.content.ContextWrapper) {
            if (context is FragmentActivity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }