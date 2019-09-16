@file:JvmName("Dialogs")

package com.app.base.widget.dialog

import android.app.Dialog
import android.content.Context
import com.app.base.R

///////////////////////////////////////////////////////////////////////////
// Loading
///////////////////////////////////////////////////////////////////////////
@JvmOverloads
fun createLoadingDialog(context: Context, autoShow: Boolean = false): Dialog {
    val loadingDialog = LoadingDialog(context)
    loadingDialog.setCanceledOnTouchOutside(false)
    loadingDialog.setMessage(R.string.dialog_loading)
    if (autoShow) {
        loadingDialog.show()
    }
    return loadingDialog
}