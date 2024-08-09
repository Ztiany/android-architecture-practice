package com.app.base.ui.dialog.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.use
import com.android.base.utils.android.views.beGone
import com.app.base.ui.dialog.R
import com.app.base.ui.dialog.databinding.DialogLayoutBottomFullBinding
import com.app.base.ui.dialog.databinding.DialogLayoutBottomPartitionBinding

/**
 * 通用的 Dialog 底部布局。
 *
 *@author Ztiany
 */
class DialogBottomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var tvPositive: TextView
        private set

    lateinit var tvNegative: TextView
        private set

    lateinit var tvNeutral: TextView
        private set

    private lateinit var viewBtnRightDivider: View
    private lateinit var viewBtnLeftDivider: View

    init {
        context.obtainStyledAttributes(attrs, R.styleable.DialogBottomView).use {
            /*
            style:
                    <enum name="full" value="1" />
                    <enum name="partitioned" value="2" />
            */
            if (it.getInt(R.styleable.DialogBottomView_dbv_style, 1) == 1) {
                with(DialogLayoutBottomFullBinding.inflate(LayoutInflater.from(context), this)) {
                    tvPositive = dialogTvPositive
                    tvNegative = dialogTvNegative
                    tvNeutral = dialogTvNeutral
                    viewBtnRightDivider = dialogViewRightDivider
                    viewBtnLeftDivider = dialogViewLeftDivider
                }
            } else {
                with(DialogLayoutBottomPartitionBinding.inflate(LayoutInflater.from(context), this)) {
                    tvPositive = dialogTvPositive
                    tvNegative = dialogTvNegative
                    tvNeutral = dialogTvNeutral
                    viewBtnRightDivider = dialogViewRightDivider
                    viewBtnLeftDivider = dialogViewLeftDivider
                }
            }
        }
    }

    fun hideNegativeButton() {
        tvNegative.beGone()
        viewBtnLeftDivider.beGone()
    }

    fun hideNeutralButton() {
        tvNeutral.beGone()
        viewBtnRightDivider.beGone()
    }

    fun hidePositiveButton() {
        tvPositive.beGone()
        viewBtnRightDivider.beGone()
    }

}