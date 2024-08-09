package com.app.base.ui.dialog

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.app.base.ui.dialog.dsl.DialogBuilder
import com.app.base.ui.dialog.dsl.bottomsheet.ListBottomSheetDialogConfig
import com.app.base.ui.dialog.impl.bottomsheet.ListBottomSheetDialog
import com.app.base.ui.dialog.impl.bottomsheet.ListBottomSheetDialogConfigImpl
import com.google.android.material.bottomsheet.BottomSheetDialog

fun ComponentActivity.listBottomSheetDialog(init: ListBottomSheetDialogConfig.() -> Unit): DialogBuilder<BottomSheetDialog> {
    return internalListBottomSheetDialog(this, this, init)
}

fun Fragment.listBottomSheetDialog(init: ListBottomSheetDialogConfig.() -> Unit): DialogBuilder<BottomSheetDialog> {
    return internalListBottomSheetDialog(requireContext(), this, init)
}

private fun internalListBottomSheetDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: ListBottomSheetDialogConfig.() -> Unit,
): DialogBuilder<BottomSheetDialog> {
    val dialogBuilder = {
        ListBottomSheetDialog(context, lifecycleOwner, ListBottomSheetDialogConfigImpl(context).apply(init).toDescription())
    }

    return object : DialogBuilder<BottomSheetDialog> {
        override fun show(): BottomSheetDialog {
            return dialogBuilder().apply { show() }
        }

        override fun build(): BottomSheetDialog {
            return dialogBuilder()
        }
    }
}