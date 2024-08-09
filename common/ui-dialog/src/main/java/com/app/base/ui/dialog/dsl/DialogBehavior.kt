package com.app.base.ui.dialog.dsl

import android.app.Dialog

typealias OnDialogDismiss = (byUser: Boolean) -> Unit

@DialogContextDslMarker
open class DialogBehavior {

    private var _isCancelable: Boolean = true
    private var _isCanceledOnTouchOutside: Boolean = true
    private var _autoDismiss: Boolean = true
    private var _onDismissListener: OnDialogDismiss? = null

    fun cancelable(isCancelable: Boolean) {
        _isCancelable = isCancelable
    }

    fun cancelOnTouchOutside(isCanceledOnTouchOutside: Boolean) {
        _isCanceledOnTouchOutside = isCanceledOnTouchOutside
    }

    fun onDismiss(listener: OnDialogDismiss) {
        _onDismissListener = listener
    }

    fun autoDismiss(value: Boolean) {
        _autoDismiss = value
    }

    fun toDialogBehaviorDescription(): DialogBehaviorDescription {
        return DialogBehaviorDescription(
            isCancelable = _isCancelable,
            isCanceledOnTouchOutside = _isCanceledOnTouchOutside,
            shouldAutoDismiss = _autoDismiss,
            onDismissListener = _onDismissListener,
        )
    }

}

open class DialogBehaviorDescription(
    val isCancelable: Boolean,
    val isCanceledOnTouchOutside: Boolean,
    val shouldAutoDismiss: Boolean,
    val onDismissListener: OnDialogDismiss?,
)

fun DialogBehaviorDescription.applyToDialog(dialog: Dialog) {
    dialog.setCancelable(isCancelable)
    dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
}
