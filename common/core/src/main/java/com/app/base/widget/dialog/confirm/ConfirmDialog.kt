package com.app.base.widget.dialog

import android.view.LayoutInflater
import android.view.View
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.beVisible
import com.android.base.utils.android.views.clearComponentDrawable
import com.android.base.utils.android.views.setLeftDrawable
import com.app.base.databinding.DialogConfirmLayoutBinding
import com.app.base.widget.dialog.BaseDialogBuilder.Companion.NO_ID

/**
 * @author Ztiany
 */
internal class ConfirmDialog(
    builder: ConfirmDialogBuilder
) : AppBaseDialog(builder.context, limitHeight = true, style = builder.style), ConfirmDialogInterface {

    private val vb = DialogConfirmLayoutBinding.inflate(LayoutInflater.from(builder.context))

    init {
        setContentView(vb.root)
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
            vb.tvConfirmDialogTitle.beVisible()
            vb.tvConfirmDialogTitle.text = title
            vb.tvConfirmDialogTitle.textSize = builder.titleSize
        }


        //message
        vb.tvConfirmDialogMessage.text = builder.message
        vb.tvConfirmDialogMessage.setTextColor(builder.messageColor)
        vb.tvConfirmDialogMessage.gravity = builder.messageGravity
        vb.tvConfirmDialogMessage.setTextColor(builder.messageColor)
        vb.tvConfirmDialogMessage.textSize = if (builder.title.isNullOrEmpty()) {
            builder.messageSize + 2F
        } else {
            builder.messageSize
        }


        //checkbox
        if (builder.checkBoxText.isNotEmpty()) {
            vb.cbConfirmDialogCheckBox.beVisible()
            vb.cbConfirmDialogCheckBox.text = builder.checkBoxText
            vb.cbConfirmDialogCheckBox.isChecked = builder.checkBoxChecked
        } else {
            vb.cbConfirmDialogCheckBox.beGone()
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

    override fun updateMessage(message: CharSequence) {
        vb.tvConfirmDialogMessage.text = message
    }

    override var positiveEnable: Boolean
        get() = vb.dblDialogBottom.positiveEnable
        set(value) {
            vb.dblDialogBottom.positiveEnable = value
        }

}