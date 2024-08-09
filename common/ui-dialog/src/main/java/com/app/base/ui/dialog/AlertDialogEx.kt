package com.app.base.ui.dialog

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.app.base.ui.dialog.dsl.DialogBuilder
import com.app.base.ui.dialog.dsl.alert.AlertDialogConfig
import com.app.base.ui.dialog.dsl.alert.AlertDialogInterface
import com.app.base.ui.dialog.impl.alert.AlertDialogConfigImpl
import com.app.base.ui.dialog.impl.alert.AppAlertDialog

fun ComponentActivity.alertDialog(init: AlertDialogConfig.() -> Unit): DialogBuilder<AlertDialogInterface> {
    return internalAlertDialog(this, this, init)
}

fun Fragment.alertDialog(init: AlertDialogConfig.() -> Unit): DialogBuilder<AlertDialogInterface> {
    return internalAlertDialog(requireContext(), this, init)
}

private fun internalAlertDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: AlertDialogConfig.() -> Unit,
): DialogBuilder<AlertDialogInterface> {
    val dialogBuilder = {
        AppAlertDialog(context, lifecycleOwner, AlertDialogConfigImpl(context).apply(init).toDescription())
    }

    return object : DialogBuilder<AlertDialogInterface> {
        override fun show(): AlertDialogInterface {
            return dialogBuilder().apply { show() }
        }

        override fun build(): AlertDialogInterface {
            return dialogBuilder()
        }
    }
}
