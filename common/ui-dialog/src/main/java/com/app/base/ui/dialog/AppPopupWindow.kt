package com.app.base.ui.dialog

import android.content.Context
import android.widget.PopupWindow
import com.app.base.ui.dialog.dsl.popup.PopupWindowInterface

abstract class AppPopupWindow<C : PopupWindowInterface>(context: Context) : PopupWindow(context) {

    abstract val controller: C

}