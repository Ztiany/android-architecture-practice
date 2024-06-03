package com.app.base.widget.dialog.loading

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialog
import com.app.base.R
import com.app.base.widget.dialog.showCompat

/**
 * @author Ztiany
 */
class LoadingDialog(context: Context?) : AppCompatDialog(context!!), LoadingDialogInterface {
    private var mMessageTv: TextView? = null

    init {
        setView()
    }

    private fun setView() {
        setContentView(R.layout.dialog_loading)
        mMessageTv = findViewById(R.id.dialog_loading_tv_title)
    }

    fun setMessage(message: CharSequence?) {
        if (!TextUtils.isEmpty(message)) {
            mMessageTv!!.text = message
        }
    }

    fun setMessage(@StringRes messageId: Int) {
        if (messageId != 0) {
            mMessageTv!!.setText(messageId)
        }
    }

    override fun show() {
        this.showCompat {
            super@LoadingDialog.show()
            Unit
        }
    }

    override val dialog: Dialog
        get() = this

    override fun updateMessage(message: CharSequence) {
        setMessage(message)
    }
}
