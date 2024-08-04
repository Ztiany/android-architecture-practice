package com.app.base.widget.dialog.loading

import android.content.Context
import com.app.base.ui.theme.R
import com.app.base.widget.dialog.loading.LoadingDialog
import com.app.base.widget.dialog.loading.LoadingDialogInterface

@JvmOverloads
fun createLoadingDialog(context: Context, autoShow: Boolean = false): LoadingDialogInterface {
    val loadingDialog = LoadingDialog(context)
    loadingDialog.setCanceledOnTouchOutside(false)
    loadingDialog.setMessage(R.string.dialog_loading)
    if (autoShow) {
        loadingDialog.show()
    }
    return loadingDialog
}