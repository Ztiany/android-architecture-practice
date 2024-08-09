package com.app.base.ui.dialog

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.app.base.ui.dialog.dsl.popup.MultiSelectionPopupWindowConfig
import com.app.base.ui.dialog.dsl.popup.MultiSelectionPopupWindowInterface
import com.app.base.ui.dialog.dsl.popup.PopupWindowWithInterface
import com.app.base.ui.dialog.dsl.popup.SingleSelectionPopupWindowConfig
import com.app.base.ui.dialog.dsl.popup.SingleSelectionPopupWindowInterface
import com.app.base.ui.dialog.impl.popup.MultiSelectionPopupConfigImpl
import com.app.base.ui.dialog.impl.popup.SelectionPopupWindow
import com.app.base.ui.dialog.impl.popup.SingleSelectionPopupWindowConfigImpl

fun ComponentActivity.buildSingleSelectionPopupWindow(
    init: SingleSelectionPopupWindowConfig.() -> Unit,
): PopupWindowWithInterface<SingleSelectionPopupWindowInterface> {
    return internalBuildSingleSelectionPopupWindow(this, this, init)
}

fun Fragment.buildSingleSelectionPopupWindow(
    init: SingleSelectionPopupWindowConfig.() -> Unit,
): PopupWindowWithInterface<SingleSelectionPopupWindowInterface> {
    return internalBuildSingleSelectionPopupWindow(requireContext(), this, init)
}

fun ComponentActivity.buildMultiSelectionPopupWindow(
    init: MultiSelectionPopupWindowConfig.() -> Unit,
): PopupWindowWithInterface<MultiSelectionPopupWindowInterface> {
    return internalBuildMultiSelectionPopupWindow(this, this, init)
}

fun Fragment.buildMultiSelectionPopupWindow(
    init: MultiSelectionPopupWindowConfig.() -> Unit,
): PopupWindowWithInterface<MultiSelectionPopupWindowInterface> {
    return internalBuildMultiSelectionPopupWindow(requireContext(), this, init)
}

private fun internalBuildSingleSelectionPopupWindow(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: SingleSelectionPopupWindowConfig.() -> Unit,
): PopupWindowWithInterface<SingleSelectionPopupWindowInterface> {
    val selectionPopupWindow = SelectionPopupWindow(
        context,
        lifecycleOwner,
        SingleSelectionPopupWindowConfigImpl(context).apply(init).toDescription()
    )
    return PopupWindowWithInterface(selectionPopupWindow, selectionPopupWindow)
}

private fun internalBuildMultiSelectionPopupWindow(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: MultiSelectionPopupWindowConfig.() -> Unit,
): PopupWindowWithInterface<MultiSelectionPopupWindowInterface> {
    val selectionPopupWindow = SelectionPopupWindow(
        context,
        lifecycleOwner,
        MultiSelectionPopupConfigImpl(context).apply(init).toDescription()
    )
    return PopupWindowWithInterface(selectionPopupWindow, selectionPopupWindow)
}