package com.app.base.ui.dialog.impl.alert

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.onThrottledClick
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import com.app.base.ui.dialog.AppBaseDialog
import com.app.base.ui.dialog.databinding.DialogLayoutAlertBinding
import com.app.base.ui.dialog.dsl.Condition
import com.app.base.ui.dialog.dsl.alert.AlertDialogDescription
import com.app.base.ui.dialog.dsl.alert.AlertDialogInterface
import com.app.base.ui.dialog.dsl.applyTo
import com.app.base.ui.dialog.dsl.applyToDialog
import com.app.base.ui.dialog.dsl.getConditionId
import com.app.base.ui.dialog.impl.DialogInterfaceWrapper


class AppAlertDialog(
    context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val description: AlertDialogDescription,
    style: Int = com.app.base.ui.theme.R.style.AppTheme_Dialog_Common_Transparent_Floating,
) : AppBaseDialog(context, description.size, style = style), AlertDialogInterface {

    private val vb by lazy {
        // It is necessary to use the context of the dialog, otherwise the theme will not be applied!
        DialogLayoutAlertBinding.inflate(LayoutInflater.from(getContext()))
    }

    private val condition = object : Condition {
        override fun isConditionMeet(id: Int): Boolean {
            if (vb.dialogCbOption.getConditionId() == id) {
                return vb.dialogCbOption.isChecked
            }
            return false
        }
    }

    private val dialogInterfaceWrapper by lazy {
        DialogInterfaceWrapper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)
        applyBuilder()

        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                dismiss()
            }
        })
    }

    private fun applyBuilder() = with(vb) {
        description.title.ifNonNull {
            applyTo(dialogTvTitle)
        } otherwise { dialogTvTitle.beGone() }

        description.messageConfig.ifNonNull {
            dialogClMessageContainer.removeAllViews()
            invoke(this@AppAlertDialog, dialogClMessageContainer)
        } otherwise {
            description.message?.applyTo(dialogTvMessage)
        }

        description.checkBox.ifNonNull {
            applyTo(dialogCbOption)
        } otherwise { dialogCbOption.beGone() }

        setUpBottomButtons()

        with(description.behavior) {
            applyToDialog(this@AppAlertDialog)
            setOnDismissListener {
                onDismissListener?.invoke(dialogInterfaceWrapper.canceledByUser)
            }
        }
    }

    private fun DialogLayoutAlertBinding.setUpBottomButtons() {
        if (description.positiveButton == null && description.negativeButton == null && description.neutralButton == null) {
            dialogBottom.beGone()
            return
        }

        with(description.positiveButton) {
            ifNonNull {
                textDescription.applyTo(dialogBottom.tvPositive)
                dialogBottom.tvPositive.onThrottledClick {
                    onClickListener?.invoke(dialogInterfaceWrapper, condition)
                    dismissChecked()
                }
            } otherwise { dialogBottom.hidePositiveButton() }
        }

        with(description.negativeButton) {
            ifNonNull {
                textDescription.applyTo(dialogBottom.tvNegative)
                dialogBottom.tvNegative.onThrottledClick {
                    onClickListener?.invoke(dialogInterfaceWrapper, condition)
                    dismissChecked()
                }
            } otherwise { dialogBottom.hideNegativeButton() }
        }

        with(description.neutralButton) {
            ifNonNull {
                textDescription.applyTo(dialogBottom.tvNeutral)
                dialogBottom.tvNeutral.onThrottledClick {
                    onClickListener?.invoke(dialogInterfaceWrapper, condition)
                    dismissChecked()
                }
            } otherwise { dialogBottom.hideNeutralButton() }
        }
    }

    private fun dismissChecked() {
        if (isShowing && description.behavior.shouldAutoDismiss) {
            dialogInterfaceWrapper.dismiss()
        }
    }

    override fun updateTitle(title: CharSequence) {
        vb.dialogTvTitle.text = title
    }

    override fun updateMessage(message: CharSequence) {
        vb.dialogTvMessage.text = message
    }

    override var isPositiveButtonEnable: Boolean
        get() = vb.dialogBottom.tvPositive.isEnabled
        set(value) {
            vb.dialogBottom.tvPositive.isEnabled = value
        }

}