package com.app.base.ui.dialog

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.app.base.ui.dialog.dsl.DialogBuilder
import com.app.base.ui.dialog.dsl.list.ListDialogConfig
import com.app.base.ui.dialog.dsl.list.ListDialogInterface
import com.app.base.ui.dialog.impl.list.AppListDialog
import com.app.base.ui.dialog.impl.list.ListDialogConfigImpl

fun ComponentActivity.listDialog(init: ListDialogConfig.() -> Unit): DialogBuilder<ListDialogInterface> {
    return internalListDialog(this, this, init)
}

fun Fragment.listDialog(init: ListDialogConfig.() -> Unit): DialogBuilder<ListDialogInterface> {
    return internalListDialog(requireContext(), this, init)
}

private fun internalListDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: ListDialogConfig.() -> Unit,
): DialogBuilder<ListDialogInterface> {
    val dialogBuilder = {
        AppListDialog(context, lifecycleOwner, ListDialogConfigImpl(context).apply(init).toDescription())
    }

    return object : DialogBuilder<ListDialogInterface> {
        override fun show(): ListDialogInterface {
            return dialogBuilder().apply { show() }
        }

        override fun build(): ListDialogInterface {
            return dialogBuilder()
        }
    }

}