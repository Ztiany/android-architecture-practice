package com.app.base.widget.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.visible
import com.app.base.databinding.DialogCommonBottomBinding

/**
 * 通用的 Dialog 底部布局
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-01-03 20:24
 */
class DialogBottomLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding = DialogCommonBottomBinding.inflate(LayoutInflater.from(context), this)

    fun positiveText(text: CharSequence?) {
        viewBinding.tvDialogPositive.text = text
    }

    var positiveEnable: Boolean
        get() = viewBinding.tvDialogPositive.isEnabled
        set(value) {
            viewBinding.tvDialogPositive.isEnabled = value
        }

    fun positiveColor(@ColorInt color: Int) {
        viewBinding.tvDialogPositive.setTextColor(color)
    }

    fun positiveColor(colors: ColorStateList) {
        viewBinding.tvDialogPositive.setTextColor(colors)
    }

    fun onPositiveClick(onClick: View.OnClickListener) {
        viewBinding.tvDialogPositive.setOnClickListener(onClick)
    }

    fun onPositiveClick(onClick: (View) -> Unit) {
        viewBinding.tvDialogPositive.setOnClickListener(onClick)
    }

    fun negativeText(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            viewBinding.tvDialogNegative.gone()
            viewBinding.viewDialogBtnLeftDivider.gone()
        } else {
            viewBinding.tvDialogNegative.visible()
            viewBinding.viewDialogBtnLeftDivider.visible()
            viewBinding.tvDialogNegative.text = text
        }
    }

    fun negativeColor(@ColorInt color: Int) {
        viewBinding.tvDialogNegative.setTextColor(color)
    }

    fun negativeColor(colors: ColorStateList) {
        viewBinding.tvDialogNegative.setTextColor(colors)
    }

    fun onNegativeClick(onClick: View.OnClickListener) {
        viewBinding.tvDialogNegative.setOnClickListener(onClick)
    }

    fun onNegativeClick(onClick: (View) -> Unit) {
        viewBinding.tvDialogNegative.setOnClickListener(onClick)
    }

    fun hideNegative() {
        negativeText(null)
    }

    fun neutralText(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            viewBinding.tvDialogNeutral.gone()
            viewBinding.viewDialogBtnRightDivider.gone()
        } else {
            viewBinding.tvDialogNeutral.visible()
            viewBinding.viewDialogBtnRightDivider.visible()
            viewBinding.tvDialogNeutral.text = text
        }
    }

    fun neutralColor(@ColorInt color: Int) {
        viewBinding.tvDialogNeutral.setTextColor(color)
    }

    fun middleColor(colors: ColorStateList) {
        viewBinding.tvDialogNeutral.setTextColor(colors)
    }

    fun onNeutralClick(onClick: View.OnClickListener) {
        viewBinding.tvDialogNeutral.setOnClickListener(onClick)
    }

    fun onNeutralClick(onClick: (View) -> Unit) {
        viewBinding.tvDialogNeutral.setOnClickListener(onClick)
    }

    fun hideNeutral() {
        neutralText(null)
    }

}