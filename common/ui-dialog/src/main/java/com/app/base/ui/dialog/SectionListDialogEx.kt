package com.app.base.ui.dialog

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.app.base.ui.dialog.dsl.DialogBuilder
import com.app.base.ui.dialog.dsl.list.MultiSelectionListDialogConfig
import com.app.base.ui.dialog.dsl.list.MultiSelectionListDialogInterface
import com.app.base.ui.dialog.dsl.list.SingleSelectionListDialogConfig
import com.app.base.ui.dialog.dsl.list.SingleSelectionListDialogInterface
import com.app.base.ui.dialog.impl.list.MultiSelectionListDialogConfigImpl
import com.app.base.ui.dialog.impl.list.SectionListDialog
import com.app.base.ui.dialog.impl.list.SingleSelectionListDialogConfigImpl

fun ComponentActivity.singleSelectionListDialog(init: SingleSelectionListDialogConfig.() -> Unit): DialogBuilder<SingleSelectionListDialogInterface> {
    return internalSingleSelectionListDialog(this, this, init)
}

fun Fragment.singleSelectionListDialog(init: SingleSelectionListDialogConfig.() -> Unit): DialogBuilder<SingleSelectionListDialogInterface> {
    return internalSingleSelectionListDialog(requireContext(), this, init)
}

fun ComponentActivity.multiSelectionListDialog(init: MultiSelectionListDialogConfig.() -> Unit): DialogBuilder<MultiSelectionListDialogInterface> {
    return internalMultiSelectionListDialog(this, this, init)
}

fun Fragment.multiSelectionListDialog(init: MultiSelectionListDialogConfig.() -> Unit): DialogBuilder<MultiSelectionListDialogInterface> {
    return internalMultiSelectionListDialog(requireContext(), this, init)
}

private fun internalSingleSelectionListDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: SingleSelectionListDialogConfig.() -> Unit,
): DialogBuilder<SingleSelectionListDialogInterface> {
    val dialogBuilder = {
        SectionListDialog(context, lifecycleOwner, SingleSelectionListDialogConfigImpl(context).apply(init).toDescription())
    }

    return object : DialogBuilder<SingleSelectionListDialogInterface> {
        override fun show(): SingleSelectionListDialogInterface {
            return dialogBuilder().apply { show() }
        }

        override fun build(): SingleSelectionListDialogInterface {
            return dialogBuilder()
        }
    }
}

private fun internalMultiSelectionListDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: MultiSelectionListDialogConfig.() -> Unit,
): DialogBuilder<MultiSelectionListDialogInterface> {
    val dialogBuilder = {
        SectionListDialog(context, lifecycleOwner, MultiSelectionListDialogConfigImpl(context).apply(init).toDescription())
    }

    return object : DialogBuilder<MultiSelectionListDialogInterface> {
        override fun show(): MultiSelectionListDialogInterface {
            return dialogBuilder().apply { show() }
        }

        override fun build(): MultiSelectionListDialogInterface {
            return dialogBuilder()
        }
    }
}