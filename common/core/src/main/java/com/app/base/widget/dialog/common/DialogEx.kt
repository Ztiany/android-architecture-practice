@file:JvmName("DialogEx")

package com.app.base.widget.dialog.common

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.android.base.utils.android.compat.AndroidVersion
import com.android.base.utils.android.views.realContext
import com.app.base.ui.R
import com.google.android.material.color.MaterialColors

open class BaseDialogBuilder(val context: Context) {

    companion object {
        internal const val NO_ID = 0
    }

    /** 样式 */
    var style: Int = R.style.AppTheme_Dialog_Common_Transparent_Floating

    /** 确认按钮 */
    @StringRes
    var positiveResId: Int = NO_ID
        set(value) {
            positiveText = if (value == NO_ID) {
                null
            } else {
                context.getText(value)
            }
        }
    var positiveText: CharSequence? = context.getText(R.string.sure)
    @ColorInt var positiveColor: Int = MaterialColors.getColor(context, R.attr.app_color_deepest, "app_color_deepest not provided.")

    /** 否认按钮 */
    @StringRes
    var negativeResId: Int = NO_ID
        set(value) {
            negativeText = if (value == NO_ID) {
                null
            } else {
                context.getText(value)
            }
        }
    var negativeText: CharSequence? = context.getText(R.string.cancel)
    @ColorInt var negativeColor: Int = MaterialColors.getColor(context, R.attr.app_color_text_variant1, "app_color_text_variant1 not provided.")

    fun disableNegative() {
        negativeText = null
    }

    /** 选择后，自动 dismiss */
    var autoDismiss: Boolean = true

    var cancelable: Boolean = true
    var cancelableTouchOutside: Boolean = true
}

fun Dialog.showCompat(superShow: () -> Unit) {
    //https://stackoverflow.com/questions/33644326/hiding-status-bar-while-showing-alert-dialog-android
    //https://stackoverflow.com/questions/22794049/how-do-i-maintain-the-immersive-mode-in-dialogs

    window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

    superShow()

    realContext?.let {
        window?.let { safeWindow ->
            safeWindow.decorView.systemUiVisibility = it.window.decorView.systemUiVisibility
            if (AndroidVersion.atLeast(28)) {
                val attributes = safeWindow.attributes
                attributes.layoutInDisplayCutoutMode = it.window.attributes.layoutInDisplayCutoutMode
                safeWindow.attributes = attributes
            }
        }
    }

    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
}