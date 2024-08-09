package com.app.base.ui.dialog.dsl

import android.content.DialogInterface

interface DialogBuilder<T : DialogInterface> {

    fun show(): T

    fun build(): T

}