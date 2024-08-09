package com.app.base.ui.dialog

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.app.base.ui.dialog.dsl.popup.OptionPopupWindowConfig
import com.app.base.ui.dialog.dsl.popup.OptionPopupWindowInterface
import com.app.base.ui.dialog.dsl.popup.PopupWindowWithInterface
import com.app.base.ui.dialog.impl.popup.OptionPopupWindow
import com.app.base.ui.dialog.impl.popup.OptionPopupWindowConfigImpl

fun ComponentActivity.buildOptionPopupWindow(
    init: OptionPopupWindowConfig.() -> Unit,
): PopupWindowWithInterface<OptionPopupWindowInterface> {
    return internalBuildOptionPopupWindow(this, this, init)
}

fun Fragment.buildOptionPopupWindow(
    init: OptionPopupWindowConfig.() -> Unit,
): PopupWindowWithInterface<OptionPopupWindowInterface> {
    return internalBuildOptionPopupWindow(requireContext(), this, init)
}

private fun internalBuildOptionPopupWindow(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    init: OptionPopupWindowConfig.() -> Unit,
): PopupWindowWithInterface<OptionPopupWindowInterface> {
    val optionPopupWindow = OptionPopupWindow(
        context,
        lifecycleOwner,
        OptionPopupWindowConfigImpl(context).apply(init).toDescription()
    )
    return PopupWindowWithInterface(optionPopupWindow, optionPopupWindow)
}