package com.app.base.widget.dialog.loading

import android.app.Dialog
import android.content.Context
import androidx.annotation.StringRes
import com.android.base.fragment.ui.LoadingViewHost
import com.app.base.ui.theme.R
import com.app.base.widget.dialog.toast.ToastKit

internal class AppLoadingViewHost(private val context: Context) : LoadingViewHost {

    private var loadingDialog: LoadingDialog? = null

    override fun showLoadingDialog(message: CharSequence, cancelable: Boolean): Dialog {
        val dialog = initLoadingDialog()
        if (message.isEmpty()) {
            dialog.setMessage(R.string.dialog_loading)
        } else {
            dialog.setMessage(message)
        }
        dialog.setCancelable(cancelable)
        if (!dialog.isShowing) {
            dialog.show()
        }
        return dialog.apply {
            loadingDialog = this
        }
    }

    override fun showLoadingDialog(@StringRes messageId: Int, cancelable: Boolean): Dialog {
        return showLoadingDialog(context.getText(messageId), cancelable)
    }

    override fun showLoadingDialog(): Dialog {
        return showLoadingDialog(true)
    }

    override fun showLoadingDialog(cancelable: Boolean): Dialog {
        return showLoadingDialog("", cancelable)
    }

    override fun dismissLoadingDialog() {
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    override fun dismissLoadingDialog(minimumMills: Long, onDismiss: (() -> Unit)?) {
        throw UnsupportedOperationException("the method should be implemented by implementer of LoadingViewHost")
    }

    override fun showMessage(message: CharSequence) {
        ToastKit.showMessage(context, message)
    }

    override fun showMessage(@StringRes messageId: Int) {
        showMessage(context.getText(messageId))
    }

    private fun initLoadingDialog(): LoadingDialog {
        var loadingDialog = loadingDialog
        if (loadingDialog == null) {
            val dialogInterface = createLoadingDialog(context, false)
            loadingDialog = dialogInterface as LoadingDialog
        }
        return loadingDialog
    }

    override fun isLoadingDialogShowing(): Boolean {
        return loadingDialog?.isShowing == true
    }

}