package com.app.base.widget.dialog.tip

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import com.app.base.databinding.DialogTipsBinding
import com.app.base.widget.dialog.TipsType
import com.app.base.widget.dialog.TipDialogBuilder
import com.app.base.widget.dialog.showCompat

/**
 * @author Ztiany
 */
internal class TipDialog(context: Context) : AppCompatDialog(context, com.app.base.ui.R.style.AppTheme_Dialog_Tips) {

    private val vb = DialogTipsBinding.inflate(LayoutInflater.from(context))

    init {
        val window = window!!
        val attributes = window.attributes
        attributes.windowAnimations = com.app.base.ui.R.style.AppAnimation_FadeIn
        setContentView(vb.root)
    }

    fun setMessage(message: CharSequence?) {
        if (!TextUtils.isEmpty(message)) {
            vb.dialogTipsTvMessage.text = message
        }
    }

    fun setMessage(@StringRes messageId: Int) {
        if (messageId != 0) {
            vb.dialogTipsTvMessage.setText(messageId)
        }
    }

    fun setTipsType(@TipsType type: Int) {
        when (type) {
            TipDialogBuilder.TYPE_SUCCESS -> {
                vb.dialogTipsTvMessage.setCompoundDrawablesWithIntrinsicBounds(0, com.app.base.ui.R.drawable.icon_tips_success, 0, 0)
                vb.dialogTipsTvMessage.setTextColor(ContextCompat.getColor(context, com.app.base.ui.R.color.white))
            }

            TipDialogBuilder.TYPE_FAILURE, TipDialogBuilder.TYPE_WARNING -> {
                vb.dialogTipsTvMessage.setCompoundDrawablesWithIntrinsicBounds(0, com.app.base.ui.R.drawable.icon_tips_failed, 0, 0)
                vb.dialogTipsTvMessage.setTextColor(ContextCompat.getColor(context, com.app.base.ui.R.color.text_stress))
            }
        }
    }

    override fun show() {
        this.showCompat {
            super@TipDialog.show()
        }
    }

}