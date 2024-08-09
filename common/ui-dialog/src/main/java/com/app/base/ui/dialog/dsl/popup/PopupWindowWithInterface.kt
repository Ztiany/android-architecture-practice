package com.app.base.ui.dialog.dsl.popup

import android.widget.PopupWindow

class PopupWindowWithInterface<T : PopupWindowInterface>(
    val popupWindow: PopupWindow,
    val controller: T,
)

fun <T : PopupWindowInterface> PopupWindowWithInterface<T>.then(block: PopupWindow.(T) -> Unit) {
    popupWindow.block(controller)
}