package com.app.base.ui.dialog.dsl

import android.app.Activity
import android.app.Dialog

typealias OnButtonClickListener = Condition.(dialog: Dialog) -> Unit

interface Condition {

    fun isConditionMet(key: String): Boolean

}

class Button(activity: Activity, text: CharSequence) : Text(activity, text) {

    internal var onClickListener: OnButtonClickListener? = null

    fun onClick(listener: OnButtonClickListener) {
        onClickListener = listener
    }

}