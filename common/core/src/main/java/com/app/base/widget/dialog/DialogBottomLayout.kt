package com.app.base.widget.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.visible
import com.app.base.R
import com.app.base.databinding.DialogCommonBottomBinding
import com.app.base.databinding.DialogCommonBottomNonfullBinding

/**
 * 通用的 Dialog 底部布局。
 *
 *@author Ztiany
 */
class DialogBottomLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var tvPositive: TextView
    private lateinit var tvNegative: TextView
    private lateinit var tvNeutral: TextView
    private lateinit var viewBtnRightDivider: View
    private lateinit var viewBtnLeftDivider: View

    init {
        context.obtainStyledAttributes(attrs, R.styleable.DialogBottomLayout).use {
            if (it.getInt(R.styleable.DialogBottomLayout_dbl_style, 1) == 1) {
                with(DialogCommonBottomBinding.inflate(LayoutInflater.from(context), this)) {
                    tvPositive = tvDialogPositive
                    tvNegative = tvDialogNegative
                    tvNeutral = tvDialogNeutral
                    viewBtnRightDivider = viewDialogBtnRightDivider
                    viewBtnLeftDivider = viewDialogBtnLeftDivider
                }
            } else {
                with(DialogCommonBottomNonfullBinding.inflate(LayoutInflater.from(context), this)) {
                    tvPositive = tvDialogPositive
                    tvNegative = tvDialogNegative
                    tvNeutral = tvDialogNeutral
                    viewBtnRightDivider = viewDialogBtnRightDivider
                    viewBtnLeftDivider = viewDialogBtnLeftDivider
                }
            }
        }
    }

    fun positiveText(text: CharSequence?) {
        tvPositive.text = text
    }

    var positiveEnable: Boolean
        get() = tvPositive.isEnabled
        set(value) {
            tvPositive.isEnabled = value
        }

    fun positiveColor(@ColorInt color: Int) {
        tvPositive.setTextColor(color)
    }

    fun positiveColor(colors: ColorStateList) {
        tvPositive.setTextColor(colors)
    }

    fun onPositiveClick(onClick: View.OnClickListener) {
        tvPositive.setOnClickListener(onClick)
    }

    fun onPositiveClick(onClick: (View) -> Unit) {
        tvPositive.setOnClickListener(onClick)
    }

    fun negativeText(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            tvNegative.gone()
            viewBtnLeftDivider.gone()
        } else {
            tvNegative.visible()
            viewBtnLeftDivider.visible()
            tvNegative.text = text
        }
    }

    fun negativeColor(@ColorInt color: Int) {
        tvNegative.setTextColor(color)
    }

    fun negativeColor(colors: ColorStateList) {
        tvNegative.setTextColor(colors)
    }

    fun onNegativeClick(onClick: View.OnClickListener) {
        tvNegative.setOnClickListener(onClick)
    }

    fun onNegativeClick(onClick: (View) -> Unit) {
        tvNegative.setOnClickListener(onClick)
    }

    fun hideNegative() {
        negativeText(null)
    }

    fun neutralText(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            tvNeutral.gone()
            viewBtnRightDivider.gone()
        } else {
            tvNeutral.visible()
            viewBtnRightDivider.visible()
            tvNeutral.text = text
        }
    }

    fun neutralColor(@ColorInt color: Int) {
        tvNeutral.setTextColor(color)
    }

    fun middleColor(colors: ColorStateList) {
        tvNeutral.setTextColor(colors)
    }

    fun onNeutralClick(onClick: View.OnClickListener) {
        tvNeutral.setOnClickListener(onClick)
    }

    fun onNeutralClick(onClick: (View) -> Unit) {
        tvNeutral.setOnClickListener(onClick)
    }

    fun hideNeutral() {
        neutralText(null)
    }

}