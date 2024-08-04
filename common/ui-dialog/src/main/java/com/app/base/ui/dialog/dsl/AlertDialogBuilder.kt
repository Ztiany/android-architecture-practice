package com.app.base.ui.dialog.dsl

interface AlertDialogBuilder : DialogContext {

    fun title(init: Text.() -> Unit)

}