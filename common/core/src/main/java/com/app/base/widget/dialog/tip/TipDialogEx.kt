package com.app.base.widget.dialog.tip

import android.app.Activity
import android.app.Dialog
import android.content.Context
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.app.base.widget.dialog.common.BaseDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@IntDef(TipDialogBuilder.TYPE_SUCCESS, TipDialogBuilder.TYPE_FAILURE, TipDialogBuilder.TYPE_WARNING)
annotation class TipType

class TipDialogBuilder internal constructor(
    val activity: Activity?,
    val fragment: Fragment?,
) {

    companion object {
        const val TYPE_SUCCESS = 1
        const val TYPE_FAILURE = 2
        const val TYPE_WARNING = 3
    }

    val context: Context
        get() = activity ?: (fragment?.requireContext() ?: throw IllegalStateException("never happen."))

    /*消息*/
    @StringRes
    var messageResId = BaseDialogBuilder.NO_ID
        set(value) {
            message = context.getText(value)
        }
    var message: CharSequence = "debug：请设置 message。"

    @TipType var type = TYPE_SUCCESS

    var onDismiss: (() -> Unit)? = null

    var autoDismissMillisecond: Long = 2000
}

fun Fragment.showTipDialog(builder: TipDialogBuilder.() -> Unit): Dialog {
    return showTipDialog(null, this, builder)
}

fun FragmentActivity.showTipDialog(builder: TipDialogBuilder.() -> Unit): Dialog {
    return showTipDialog(this, null, builder)
}

private fun showTipDialog(activity: Activity?, fragment: Fragment?, builder: TipDialogBuilder.() -> Unit): Dialog {
    val tipDialogBuilder = TipDialogBuilder(activity, fragment)
    builder(tipDialogBuilder)
    return showTipsDialogImpl(tipDialogBuilder)
}

private fun showTipsDialogImpl(tipDialogBuilder: TipDialogBuilder): Dialog {
    val tipDialog = TipDialog(tipDialogBuilder.context)

    tipDialog.setMessage(tipDialogBuilder.message)
    tipDialog.setCancelable(false)
    tipDialog.setTipsType(tipDialogBuilder.type)

    val lifecycleOwner: LifecycleOwner = tipDialogBuilder.fragment ?: tipDialogBuilder.context as FragmentActivity

    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            tipDialog.dismiss()
        }
    })

    tipDialog.setOnDismissListener {
        tipDialogBuilder.onDismiss?.invoke()
    }

    tipDialog.show()

    lifecycleOwner.lifecycleScope.launch {
        delay(tipDialogBuilder.autoDismissMillisecond)
        tipDialog.dismiss()
    }

    return tipDialog
}
