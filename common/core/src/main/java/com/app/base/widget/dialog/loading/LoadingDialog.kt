package com.app.base.widget.dialog.loading

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialog
import com.app.base.databinding.DialogLoadingBinding
import com.app.base.widget.dialog.common.showCompat

/**
 * @author Ztiany
 */
internal class LoadingDialog(context: Context) : AppCompatDialog(context), LoadingDialogInterface {

    private val vb = DialogLoadingBinding.inflate(LayoutInflater.from(context))

    init {
        setView()
    }

    private fun setView() {
        setContentView(vb.root)
    }

    fun setMessage(message: CharSequence?) {
        if (!TextUtils.isEmpty(message)) {
            vb.dialogLoadingTvTitle.text = message
        }
    }

    fun setMessage(@StringRes messageId: Int) {
        if (messageId != 0) {
            vb.dialogLoadingTvTitle.setText(messageId)
        }
    }

    override fun show() {
        this.showCompat {
            super@LoadingDialog.show()
        }
    }

    override val dialog: Dialog
        get() = this

    override fun updateMessage(message: CharSequence) {
        setMessage(message)
    }

}