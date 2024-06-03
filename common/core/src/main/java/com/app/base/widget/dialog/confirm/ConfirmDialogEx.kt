package com.app.base.widget.dialog.confirm

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.app.base.ui.R
import com.app.base.widget.dialog.BaseDialogBuilder
import com.google.android.material.color.MaterialColors

class ConfirmDialogBuilder(context: Context) : BaseDialogBuilder(context) {

    ///////////////////////////////////////////////////////////////////////////
    // title
    ///////////////////////////////////////////////////////////////////////////
    /** 标题 id */
    @StringRes
    var titleResId: Int = NO_ID
        set(value) {
            title = context.getText(value)
        }
    var title: CharSequence? = null

    /** 标题的字体大小，单位为 sp */
    var titleSize = 18F

    /** 标题的字体颜色 */
    var titleColor = MaterialColors.getColor(context, R.attr.app_color_deepest, "app_color_deepest not provided.")

    ///////////////////////////////////////////////////////////////////////////
    // message
    ///////////////////////////////////////////////////////////////////////////
    @StringRes
    var messageResId = NO_ID
        set(value) {
            message = context.getText(value)
        }
    var message: CharSequence = "debug：请设置 message。"

    /** 消息的字体大小，单位为 sp */
    var messageSize = 14F

    /** 消息的字体颜色 */
    var messageColor = MaterialColors.getColor(context, R.attr.app_color_text_level2, "app_color_text_level2 not provided.")

    /** 消息对其方式 */
    var messageGravity: Int = Gravity.CENTER

    ///////////////////////////////////////////////////////////////////////////
    //中间的按钮
    ///////////////////////////////////////////////////////////////////////////
    /** 中间的按钮 */
    @StringRes
    var neutralResId: Int = NO_ID
        set(value) {
            neutralText = if (value == NO_ID) {
                null
            } else {
                context.getText(value)
            }
        }
    var neutralText: CharSequence? = null
    @ColorInt var neutralColor: Int = MaterialColors.getColor(context, R.attr.app_color_text_variant1, "app_color_text_variant1 not provided.")

    ///////////////////////////////////////////////////////////////////////////
    // checkbox
    ///////////////////////////////////////////////////////////////////////////
    @StringRes
    var checkBoxResId = NO_ID
        set(value) {
            checkBoxText = context.getText(value)
        }
    var checkBoxText: CharSequence = ""

    /**  CheckBox 的字体大小，单位为 sp */
    var checkBoxTextSize = 14F
    var checkBoxChecked = false

    //确认与取消
    var positiveListener: ((dialog: Dialog) -> Unit)? = null
    var positiveListener2: ((dialog: Dialog, isChecked: Boolean) -> Unit)? = null
    var negativeListener: ((dialog: Dialog) -> Unit)? = null
    var neutralListener: ((dialog: Dialog) -> Unit)? = null

    //顶部 icon
    var iconId = NO_ID
}

fun Fragment.showConfirmDialog(builder: ConfirmDialogBuilder.() -> Unit): ConfirmDialogInterface? {
    val context = this.context ?: return null
    return showConfirmDialog(context, builder)
}

fun Activity.showConfirmDialog(builder: ConfirmDialogBuilder.() -> Unit): ConfirmDialogInterface {
    return showConfirmDialog(this, builder)
}

fun showConfirmDialog(context: Context, builder: ConfirmDialogBuilder.() -> Unit): ConfirmDialogInterface {
    val confirmDialogBuilder = ConfirmDialogBuilder(context)
    builder.invoke(confirmDialogBuilder)
    val confirmDialog = ConfirmDialog(confirmDialogBuilder)
    confirmDialog.show()
    return confirmDialog
}