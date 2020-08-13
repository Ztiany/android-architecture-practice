package com.app.base.widget.dialog

import android.view.View
import com.android.base.utils.android.views.*
import com.app.base.R
import com.app.base.widget.dialog.BaseDialogBuilder.Companion.NO_ID
import kotlinx.android.synthetic.main.dialog_confirm_layout.*

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-29 10:49
 */
internal class ConfirmDialog(builder: ConfirmDialogBuilder) : BaseDialog(builder.context, true, builder.style) {

    init {
        setContentView(R.layout.dialog_confirm_layout)
        applyBuilder(builder)
    }

    private fun applyBuilder(builder: ConfirmDialogBuilder) {
        //icon
        if (builder.iconId != NO_ID) {
            tvConfirmDialogTitle.setLeftDrawable(builder.iconId)
        } else {
            tvConfirmDialogTitle.clearComponentDrawable()
        }

        //title
        val title = builder.title
        if (title != null) {
            tvConfirmDialogTitle.visible()
            tvConfirmDialogTitle.text = title
            if (builder.titleSize != 0F) {
                tvConfirmDialogTitle.textSize = builder.titleSize
            } else {
                tvConfirmDialogTitle.textSize = 16F
            }
            if (builder.titleColor != 0) {
                tvConfirmDialogTitle.setTextColor(builder.titleColor)
            } else {
                tvConfirmDialogTitle.setTextColor(context.colorFromId(R.color.colorPrimary))
            }
        }

        //message
        tvConfirmDialogMessage.text = builder.message
        tvConfirmDialogMessage.gravity = builder.messageGravity
        if (builder.messageSize != 0F) {
            tvConfirmDialogMessage.textSize = builder.titleSize
        } else {
            tvConfirmDialogMessage.textSize = if (builder.title.isNullOrEmpty()) {
                16F
            } else {
                14F
            }
        }
        if (builder.titleColor != 0) {
            tvConfirmDialogMessage.setTextColor(builder.titleColor)
        } else {
            tvConfirmDialogMessage.setTextColor(if (builder.title.isNullOrEmpty()) {
                context.colorFromId(R.color.colorPrimary)
            } else {
                context.colorFromId(R.color.colorPrimaryDark)
            })
        }

        //checkbox
        if (builder.checkBoxText.isNotEmpty()) {
            cbConfirmDialogCheckBox.visible()
            cbConfirmDialogCheckBox.text = builder.checkBoxText
            cbConfirmDialogCheckBox.isChecked = builder.checkBoxChecked
        } else {
            cbConfirmDialogCheckBox.gone()
        }

        //cancel
        val negativeText = builder.negativeText
        if (negativeText != null) {
            dblDialogBottom.negativeText(negativeText)
            dblDialogBottom.onNegativeClick(View.OnClickListener {
                dismissChecked(builder)
                builder.negativeListener?.invoke(this)
                builder.negativeListener2?.invoke(this, cbConfirmDialogCheckBox.isChecked)
            })
        } else {
            dblDialogBottom.hideNegative()
        }

        //confirm
        dblDialogBottom.positiveText(builder.positiveText)
        dblDialogBottom.onPositiveClick(View.OnClickListener {
            dismissChecked(builder)
            builder.positiveListener?.invoke(this)
            builder.positiveListener2?.invoke(this, cbConfirmDialogCheckBox.isChecked)
        })

        setCanceledOnTouchOutside(builder.cancelableTouchOutside)
        setCancelable(builder.cancelable)
    }

    private fun dismissChecked(builder: ConfirmDialogBuilder) {
        if (builder.autoDismiss) {
            dismiss()
        }
    }

}
