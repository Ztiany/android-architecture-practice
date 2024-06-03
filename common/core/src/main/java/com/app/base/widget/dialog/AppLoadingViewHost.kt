package com.app.base.widget.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import androidx.annotation.StringRes
import com.android.base.fragment.ui.LoadingViewHost
import com.android.base.fragment.ui.Message
import com.app.base.ui.R
import com.app.base.widget.dialog.loading.LoadingDialog
import timber.log.Timber
import kotlin.jvm.functions.Function0

class AppLoadingViewHost(private val mContext: Context) : LoadingViewHost {
    private var mLoadingDialog: LoadingDialog? = null

    override fun showLoadingDialog(message: CharSequence, cancelable: Boolean): Dialog {
        initDialog()
        if (TextUtils.isEmpty(message)) {
            mLoadingDialog!!.setMessage(R.string.dialog_loading)
        } else {
            mLoadingDialog!!.setMessage(message)
        }
        mLoadingDialog!!.setCancelable(cancelable)
        if (!mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.show()
        }
        return mLoadingDialog!!
    }

    override fun showLoadingDialog(@StringRes messageId: Int, cancelable: Boolean): Dialog {
        return showLoadingDialog(mContext.getText(messageId), cancelable)
    }

    override fun showLoadingDialog(): Dialog {
        return showLoadingDialog(true)
    }

    override fun showLoadingDialog(cancelable: Boolean): Dialog {
        return showLoadingDialog("", cancelable)
    }

    override fun dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.dismiss()
        }
    }

    override fun showMessage(message: CharSequence) {
        ToastKit.showMessage(mContext, message)
    }

    override fun showMessage(@StringRes messageId: Int) {
        showMessage(mContext.getText(messageId))
    }

    private fun initDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = createLoadingDialog(mContext, false) as LoadingDialog
        }
    }

    override fun dismissLoadingDialog(minimumMills: Long, onDismiss: Function0<Unit>?) {
        throw UnsupportedOperationException("the method should be implemented by implementer of LoadingViewHost")
    }

    override fun isLoadingDialogShowing(): Boolean {
        return mLoadingDialog != null && mLoadingDialog!!.isShowing
    }

    /*
     TODO: implement this method.
     */
    override fun showMessage(message: Message) {
        Timber.d("showMessage(Message %s) is not implemented.", message)
    }
}