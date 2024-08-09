package com.app.base.ui.dialog.dsl.bottomsheet

import android.app.Dialog
import com.android.base.utils.android.views.onGlobalLayoutOnce
import com.app.base.ui.dialog.dsl.DialogBehavior
import com.app.base.ui.dialog.dsl.DialogBehaviorDescription
import com.app.base.ui.dialog.dsl.OnDialogDismiss
import com.app.base.ui.dialog.dsl.applyToDialog
import com.app.base.ui.dialog.impl.bottomsheet.AppBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetDialogBehavior : DialogBehavior() {

    private var _skipCollapsed: Boolean = false

    private var _expandedDirectly: Boolean = false

    fun skipCollapsed(skipCollapsed: Boolean) {
        _skipCollapsed = skipCollapsed
    }

    fun expandedDirectly(expandedDirectly: Boolean) {
        _expandedDirectly = expandedDirectly
    }

    fun toBottomSheetDialogBehaviorDescription(): BottomSheetDialogBehaviorDescription {
        return BottomSheetDialogBehaviorDescription(
            isCancelable = super.toDialogBehaviorDescription().isCancelable,
            isCanceledOnTouchOutside = super.toDialogBehaviorDescription().isCanceledOnTouchOutside,
            shouldAutoDismiss = super.toDialogBehaviorDescription().shouldAutoDismiss,
            onDismissListener = super.toDialogBehaviorDescription().onDismissListener,
            shouldSkipCollapsed = _skipCollapsed,
            expandedDirectly = _expandedDirectly,
        )
    }

}

class BottomSheetDialogBehaviorDescription(
    isCancelable: Boolean,
    isCanceledOnTouchOutside: Boolean,
    shouldAutoDismiss: Boolean,
    onDismissListener: OnDialogDismiss?,
    val shouldSkipCollapsed: Boolean,
    val expandedDirectly: Boolean,
) : DialogBehaviorDescription(isCancelable, isCanceledOnTouchOutside, shouldAutoDismiss, onDismissListener)

fun BottomSheetDialogBehaviorDescription.applyToDialog(bottomSheetDialog: AppBottomSheetDialog) {
    applyToDialog(bottomSheetDialog as Dialog)
    with(bottomSheetDialog.behavior) {
        skipCollapsed = shouldSkipCollapsed
    }

    if (expandedDirectly) {
        bottomSheetDialog.window?.decorView?.onGlobalLayoutOnce {
            bottomSheetDialog.behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }
}