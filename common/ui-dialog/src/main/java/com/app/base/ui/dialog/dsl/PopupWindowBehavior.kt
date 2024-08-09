package com.app.base.ui.dialog.dsl

import android.widget.PopupWindow

typealias OnPopupWindowDismiss = (byUser: Boolean) -> Unit

class PopupWindowBehavior(
    private var _isOutsideTouchable: Boolean = true,
    private var _isFocusable: Boolean = true,
    private var _isTouchable: Boolean = true,
    private var _isAutoDismiss: Boolean = true,
) {

    private var _onDismissListener: OnPopupWindowDismiss? = null

    fun isOutsideTouchable(value: Boolean) {
        _isOutsideTouchable = value
    }

    fun isFocusable(value: Boolean) {
        _isFocusable = value
    }

    fun isTouchable(value: Boolean) {
        _isTouchable = value
    }

    fun isAutoDismiss(value: Boolean) {
        _isAutoDismiss = value
    }

    fun onDismiss(onDismiss: OnPopupWindowDismiss) {
        _onDismissListener = onDismiss
    }

    fun toPopupWindowBehaviorDescription(): PopupWindowBehaviorDescription {
        return PopupWindowBehaviorDescription(
            isOutsideTouchable = _isOutsideTouchable,
            isFocusable = _isFocusable,
            isTouchable = _isTouchable,
            shouldAutoDismiss = _isAutoDismiss,
            onDismissListener = _onDismissListener
        )
    }

}

class PopupWindowBehaviorDescription(
    val isOutsideTouchable: Boolean,
    val isFocusable: Boolean,
    val isTouchable: Boolean,
    val shouldAutoDismiss: Boolean,
    val onDismissListener: OnPopupWindowDismiss?,
) {

    fun applyTo(popupWindow: PopupWindow) {
        popupWindow.isOutsideTouchable = isOutsideTouchable
        popupWindow.isFocusable = isFocusable
        popupWindow.isTouchable = isTouchable
    }

}