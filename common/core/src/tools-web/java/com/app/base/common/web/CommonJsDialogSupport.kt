package com.app.base.common.web

import android.webkit.JsResult
import com.app.base.widget.dialog.confirm.showConfirmDialog
import com.app.base.widget.dialog.list.showListDialog

internal fun onJsAlert(fragment: BaseWebFragment, message: String, result: JsResult) = with(fragment) {
    showConfirmDialog {
        cancelable = false
        this.message = message
        disableNegative()
        positiveListener = {
            result.confirm()
        }
    }
}

internal fun onJsConfirm(fragment: BaseWebFragment, message: String, result: JsResult) = with(fragment) {
    showConfirmDialog {
        cancelable = false
        this.message = message
        negativeListener = {
            result.cancel()
        }
        positiveListener = {
            result.confirm()
        }
    }
}

internal fun showTakeMediaDialog(fragment: BaseWebFragment, onTakePhoto: Runnable, onSelectPhoto: Runnable, onCancel: Runnable) = with(fragment) {
    showListDialog {
        items = arrayOf(getString(com.app.base.ui.theme.R.string.camera), getString(com.app.base.ui.theme.R.string.album))
        cancelable = false
        positiveListener = { which, _ ->
            if (which == 0) {
                onTakePhoto.run()
            } else {
                onSelectPhoto.run()
            }
        }
        negativeListener = {
            onCancel.run()
        }
    }
}