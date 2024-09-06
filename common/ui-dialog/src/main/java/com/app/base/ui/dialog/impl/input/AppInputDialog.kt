package com.app.base.ui.dialog.impl.input

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.base.utils.android.SoftKeyboardUtils
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.onThrottledClick
import com.android.base.utils.android.views.textWatcher
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import com.app.base.ui.dialog.AppBaseDialog
import com.app.base.ui.dialog.databinding.DialogLayoutInputBinding
import com.app.base.ui.dialog.dsl.Condition
import com.app.base.ui.dialog.dsl.applyTo
import com.app.base.ui.dialog.dsl.applyToDialog
import com.app.base.ui.dialog.dsl.getConditionId
import com.app.base.ui.dialog.dsl.input.InputDialogDescription
import com.app.base.ui.dialog.dsl.input.InputDialogInterface
import com.app.base.ui.dialog.impl.DialogInterfaceWrapper

/**
 *  As explained in the [how-to-resize-alertdialog-on-the-keyboard-display](https://stackoverflow.com/questions/5622202/how-to-resize-alertdialog-on-the-keyboard-display),
 *  here we pass false for the parameter `compatMode` in the constructor of the super class [AppBaseDialog].
 */
class AppInputDialog(
    context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val description: InputDialogDescription,
    style: Int = com.app.base.ui.theme.R.style.AppTheme_Dialog_Common_Transparent_Floating_Input,
) : AppBaseDialog(context, description.size, false, style), InputDialogInterface {

    private val vb by lazy {
        // It is necessary to use the context of the dialog, otherwise the theme will not be applied!
        DialogLayoutInputBinding.inflate(LayoutInflater.from(getContext()))
    }

    private val condition = object : Condition {
        override fun getFieldValue(id: Int): CharSequence {
            if (vb.dialogEtField.getConditionId() == id) {
                return vb.dialogEtField.text.toString()
            }
            return super.getFieldValue(id)
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

        if (description.behavior.forceInput) {
            dialogEtField.textWatcher {
                afterTextChanged {
                    vb.dialogBottom.tvPositive.isEnabled = !it.isNullOrEmpty()
                }
            }
        }
        description.filed?.applyTo(dialogEtField)

        setUpFieldDecor()
        setUpBottomButtons()

        with(description.behavior.dialogBehavior) {
            applyToDialog(this@AppInputDialog)
            setOnDismissListener {
                onDismissListener?.invoke(dialogInterfaceWrapper.canceledByUser)
            }
        }
    }

    private fun setUpFieldDecor() = with(description) {
        fieldTopAreaConfig?.invoke(this@AppInputDialog, (vb.dialogClFieldTopDecor))
        fieldBottomAreaConfig?.invoke(this@AppInputDialog, (vb.dialogClFieldBottomDecor))
    }

    private fun DialogLayoutInputBinding.setUpBottomButtons() {
        if (description.positiveButton == null && description.negativeButton == null) {
            dialogBottom.beGone()
            return
        }

        dialogBottom.hideNeutralButton()

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
    }

    private fun dismissChecked() {
        if (isShowing && description.behavior.dialogBehavior.shouldAutoDismiss) {
            dialogInterfaceWrapper.dismiss()
        }
    }

    override fun updateTitle(title: CharSequence) {
        vb.dialogTvTitle.text = title
    }

    override fun dismiss() {
        SoftKeyboardUtils.hideSoftInput(vb.dialogEtField)
        super.dismiss()
    }

    override fun show() {
        super.show()
        if (description.behavior.showSoftInputAfterShow) {
            vb.root.postDelayed({
                vb.dialogEtField.requestFocus()
                SoftKeyboardUtils.showSoftInput(vb.dialogEtField)
            }, 300)
        }
    }

    override var isPositiveButtonEnable: Boolean
        get() = vb.dialogBottom.tvPositive.isEnabled
        set(value) {
            vb.dialogBottom.tvPositive.isEnabled = value
        }

}