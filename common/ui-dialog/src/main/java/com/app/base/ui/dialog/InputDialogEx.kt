package com.app.base.ui.dialog

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.app.base.ui.dialog.dsl.DialogBuilder
import com.app.base.ui.dialog.dsl.input.InputDialogConfig
import com.app.base.ui.dialog.dsl.input.InputDialogInterface
import com.app.base.ui.dialog.impl.input.AppInputDialog
import com.app.base.ui.dialog.impl.input.InputDialogConfigImpl

fun ComponentActivity.inputDialog(init: InputDialogConfig.() -> Unit): DialogBuilder<InputDialogInterface> {
    return internalInputDialog(this, this, init)
}

fun Fragment.inputDialog(init: InputDialogConfig.() -> Unit): DialogBuilder<InputDialogInterface> {
    return internalInputDialog(requireContext(), this, init)
}

private fun internalInputDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: InputDialogConfig.() -> Unit,
): DialogBuilder<InputDialogInterface> {
    val dialogBuilder = {
        AppInputDialog(context, lifecycleOwner, InputDialogConfigImpl(context).apply(init).toDescription())
    }

    return object : DialogBuilder<InputDialogInterface> {
        override fun show(): AppInputDialog {
            return dialogBuilder().apply { show() }
        }

        override fun build(): InputDialogInterface {
            return dialogBuilder()
        }
    }
}
