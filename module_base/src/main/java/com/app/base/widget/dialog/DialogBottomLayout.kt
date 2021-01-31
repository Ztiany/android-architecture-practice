package com.app.base.widget.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.visible
import com.app.base.R
import kotlinx.android.synthetic.main.dialog_common_bottom.view.*

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

    init {
        View.inflate(context, R.layout.dialog_common_bottom, this)
    }

    fun positiveText(text: CharSequence?) {
        tvDialogPositive.text = text
    }

    var positiveEnable: Boolean
        get() = tvDialogPositive.isEnabled
        set(value) {
            tvDialogPositive.isEnabled = value
        }

    fun positiveColor(@ColorInt color: Int) {
        tvDialogPositive.setTextColor(color)
    }

    fun positiveColor(colors: ColorStateList) {
        tvDialogPositive.setTextColor(colors)
    }

    fun onPositiveClick(onClick: View.OnClickListener) {
        tvDialogPositive.setOnClickListener(onClick)
    }

    fun onPositiveClick(onClick: (View) -> Unit) {
        tvDialogPositive.setOnClickListener(onClick)
    }

    fun negativeText(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            tvDialogNegative.gone()
            viewDialogBtnLeftDivider.gone()
        } else {
            tvDialogNegative.visible()
            viewDialogBtnLeftDivider.visible()
            tvDialogNegative.text = text
        }
    }

    fun negativeColor(@ColorInt color: Int) {
        tvDialogNegative.setTextColor(color)
    }

    fun negativeColor(colors: ColorStateList) {
        tvDialogNegative.setTextColor(colors)
    }

    fun onNegativeClick(onClick: View.OnClickListener) {
        tvDialogNegative.setOnClickListener(onClick)
    }

    fun onNegativeClick(onClick: (View) -> Unit) {
        tvDialogNegative.setOnClickListener(onClick)
    }

    fun hideNegative() {
        negativeText(null)
    }

    fun neutralText(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            tvDialogNeutral.gone()
            viewDialogBtnRightDivider.gone()
        } else {
            tvDialogNeutral.visible()
            viewDialogBtnRightDivider.visible()
            tvDialogNeutral.text = text
        }
    }

    fun neutralColor(@ColorInt color: Int) {
        tvDialogNeutral.setTextColor(color)
    }

    fun middleColor(colors: ColorStateList) {
        tvDialogNeutral.setTextColor(colors)
    }

    fun onNeutralClick(onClick: View.OnClickListener) {
        tvDialogNeutral.setOnClickListener(onClick)
    }

    fun onNeutralClick(onClick: (View) -> Unit) {
        tvDialogNeutral.setOnClickListener(onClick)
    }

    fun hideNeutral() {
        neutralText(null)
    }

}