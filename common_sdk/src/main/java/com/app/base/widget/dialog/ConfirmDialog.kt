package com.app.base.widget.dialog

import android.view.View
import com.android.base.architecture.ui.viewBinding
import com.android.base.utils.android.views.clearComponentDrawable
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.setLeftDrawable
import com.android.base.utils.android.views.visible
import com.app.base.R
import com.app.base.databinding.DialogConfirmLayoutBinding
import com.app.base.widget.dialog.BaseDialogBuilder.Companion.NO_ID

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-29 10:49
 */
internal class ConfirmDialog(
    builder: ConfirmDialogBuilder
) : BaseDialog(builder.context, true, builder.style) {

    private val vb by viewBinding(DialogConfirmLayoutBinding::bind)

    init {
        setContentView(R.layout.dialog_confirm_layout)
        applyBuilder(builder)
    }

    private fun applyBuilder(builder: ConfirmDialogBuilder) {
        //icon
        if (builder.iconId != NO_ID) {
            vb.tvConfirmDialogTitle.setLeftDrawable(builder.iconId)
        } else {
            vb.tvConfirmDialogTitle.clearComponentDrawable()
        }

        //title
        val title = builder.title
        vb.tvConfirmDialogTitle.setTextColor(builder.titleColor)
        if (title != null) {
            vb.tvConfirmDialogTitle.visible()
            vb.tvConfirmDialogTitle.text = title
            if (builder.titleSize > 0F) {
                vb.tvConfirmDialogTitle.textSize = builder.titleSize
            } else {
                vb.tvConfirmDialogTitle.textSize = 16F
            }
        }

        //message
        vb.tvConfirmDialogMessage.text = builder.message
        vb.tvConfirmDialogMessage.setTextColor(builder.messageColor)
        vb.tvConfirmDialogMessage.gravity = builder.messageGravity
        vb.tvConfirmDialogMessage.setTextColor(builder.messageColor)
        if (builder.messageSize > 0F) {
            vb.tvConfirmDialogMessage.textSize = builder.titleSize
        } else {
            vb.tvConfirmDialogMessage.textSize = if (builder.title.isNullOrEmpty()) {
                16F
            } else {
                14F
            }
        }

        //checkbox
        if (builder.checkBoxText.isNotEmpty()) {
            vb.cbConfirmDialogCheckBox.visible()
            vb.cbConfirmDialogCheckBox.text = builder.checkBoxText
            vb.cbConfirmDialogCheckBox.isChecked = builder.checkBoxChecked
        } else {
            vb.cbConfirmDialogCheckBox.gone()
        }

        //cancel
        val negativeText = builder.negativeText
        if (negativeText != null) {
            vb.dblDialogBottom.negativeText(negativeText)
            vb.dblDialogBottom.onNegativeClick(View.OnClickListener {
                dismissChecked(builder)
                builder.negativeListener?.invoke(this)
            })
        } else {
            vb.dblDialogBottom.hideNegative()
        }
        vb.dblDialogBottom.negativeColor(builder.negativeColor)

        //neutral
        val neutralText = builder.neutralText
        if (neutralText != null) {
            vb.dblDialogBottom.neutralText(neutralText)
            vb.dblDialogBottom.onNeutralClick(View.OnClickListener {
                dismissChecked(builder)
                builder.neutralListener?.invoke(this)
            })
        } else {
            vb.dblDialogBottom.hideNeutral()
        }
        vb.dblDialogBottom.neutralColor(builder.neutralColor)

        //confirm
        vb.dblDialogBottom.positiveText(builder.positiveText)
        vb.dblDialogBottom.onPositiveClick(View.OnClickListener {
            dismissChecked(builder)
            builder.positiveListener?.invoke(this)
            builder.positiveListener2?.invoke(this, vb.cbConfirmDialogCheckBox.isChecked)
        })
        vb.dblDialogBottom.positiveColor(builder.positiveColor)

        //cancelable
        setCanceledOnTouchOutside(builder.cancelableTouchOutside)
        setCancelable(builder.cancelable)
    }

    private fun dismissChecked(builder: ConfirmDialogBuilder) {
        if (builder.autoDismiss) {
            dismiss()
        }
    }

}