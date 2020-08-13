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

    fun positiveColor(@ColorInt color: Int) {
        tvDialogPositive.setTextColor(color)
    }

    fun positiveColor(colors: ColorStateList) {
        tvDialogPositive.setTextColor(colors)
    }

    fun negativeText(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            tvDialogNegative.gone()
            viewDialogBtnDivider.gone()
        } else {
            tvDialogNegative.visible()
            viewDialogBtnDivider.visible()
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

    fun onPositiveClick(onClick: View.OnClickListener) {
        tvDialogPositive.setOnClickListener(onClick)
    }

    fun onNegativeClick(onClick: (View) -> Unit) {
        tvDialogNegative.setOnClickListener(onClick)
    }

    fun onPositiveClick(onClick: (View) -> Unit) {
        tvDialogPositive.setOnClickListener(onClick)
    }

    fun hideNegative() {
        negativeText(null)
    }

    var positiveEnable: Boolean
        get() = tvDialogPositive.isEnabled
        set(value) {
            tvDialogPositive.isEnabled = value
        }

}