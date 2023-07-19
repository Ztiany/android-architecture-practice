package com.app.base.common.web

import android.webkit.JsResult
import com.app.base.R
import com.app.base.widget.dialog.showConfirmDialog
import com.app.base.widget.dialog.showListDialog

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
        items = arrayOf(getString(R.string.camera), getString(R.string.album))
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