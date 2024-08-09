package com.app.base.ui.dialog

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.app.base.ui.dialog.dsl.DialogBuilder
import com.app.base.ui.dialog.dsl.bottomsheet.MultiSelectionBottomSheetDialogConfig
import com.app.base.ui.dialog.dsl.bottomsheet.SingleSelectionBottomSheetDialogConfig
import com.app.base.ui.dialog.impl.bottomsheet.MultiSelectionBottomSheetDialogConfigImpl
import com.app.base.ui.dialog.impl.bottomsheet.SectionBottomSheetDialog
import com.app.base.ui.dialog.impl.bottomsheet.SingleSelectionBottomSheetDialogConfigImpl
import com.google.android.material.bottomsheet.BottomSheetDialog

fun ComponentActivity.singleSelectionBottomSheetDialog(init: SingleSelectionBottomSheetDialogConfig.() -> Unit): DialogBuilder<BottomSheetDialog> {
    return internalSingleSelectionBottomSheetDialog(this, this, init)
}

fun Fragment.singleSelectionBottomSheetDialog(init: SingleSelectionBottomSheetDialogConfig.() -> Unit): DialogBuilder<BottomSheetDialog> {
    return internalSingleSelectionBottomSheetDialog(requireContext(), this, init)
}

fun ComponentActivity.multiSelectionBottomSheetDialog(init: MultiSelectionBottomSheetDialogConfig.() -> Unit): DialogBuilder<BottomSheetDialog> {
    return internalMultiSelectionBottomSheetDialog(this, this, init)
}

fun Fragment.multiSelectionBottomSheetDialog(init: MultiSelectionBottomSheetDialogConfig.() -> Unit): DialogBuilder<BottomSheetDialog> {
    return internalMultiSelectionBottomSheetDialog(requireContext(), this, init)
}

private fun internalSingleSelectionBottomSheetDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: SingleSelectionBottomSheetDialogConfig.() -> Unit,
): DialogBuilder<BottomSheetDialog> {
    val dialogBuilder = {
        SectionBottomSheetDialog(context, lifecycleOwner, SingleSelectionBottomSheetDialogConfigImpl(context).apply(init).toDescription())
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

private fun internalMultiSelectionBottomSheetDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: MultiSelectionBottomSheetDialogConfig.() -> Unit,
): DialogBuilder<BottomSheetDialog> {
    val dialogBuilder = {
        SectionBottomSheetDialog(context, lifecycleOwner, MultiSelectionBottomSheetDialogConfigImpl(context).apply(init).toDescription())
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