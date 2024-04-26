package com.app.base.widget.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.use
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.beVisible
import com.android.base.utils.android.views.beVisibleOrGone
import com.android.base.utils.android.views.dip
import com.android.base.utils.android.views.getColorCompat
import com.android.base.utils.android.views.getDimensionPixelSize
import com.android.base.utils.android.views.layoutInflater
import com.android.base.utils.android.views.textValue
import com.app.base.R
import com.app.base.databinding.WidgetTextItemBinding
import com.app.base.ui.R as UI_R

/**
 *@author Ztiany
 */
class TextItemLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val viewBinding = WidgetTextItemBinding.inflate(layoutInflater(), this)

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TextItemLayout).use(::fillAttributes)
    }

    private fun fillAttributes(typedArray: TypedArray) {
        //title
        viewBinding.widgetTextItemTvTitle.text = typedArray.getString(R.styleable.TextItemLayout_til_title)
        typedArray.getColor(R.styleable.TextItemLayout_til_title_color, -1).let {
            if (it != -1) {
                viewBinding.widgetTextItemTvTitle.setTextColor(it)
            }
        }
        if (typedArray.getBoolean(R.styleable.TextItemLayout_til_title_in_middle, false)) {
            adjustTitleToMiddle()
        }
        //subtitle
        viewBinding.widgetTextItemTvSubtitle.text = typedArray.getString(R.styleable.TextItemLayout_til_subtitle)
        typedArray.getColor(R.styleable.TextItemLayout_til_subtitle_color, -1).let {
            if (it != -1) {
                viewBinding.widgetTextItemTvSubtitle.setTextColor(it)
            }
        }
        //arrow
        viewBinding.widgetTextItemIvArrow.beVisibleOrGone(typedArray.getBoolean(R.styleable.TextItemLayout_til_show_arrow, false))
        //line
        val visibleBottomLine = typedArray.getBoolean(R.styleable.TextItemLayout_til_show_bottom_line, false)
        if (visibleBottomLine) {
            viewBinding.widgetTextItemLine.beVisible()
            val bottomLineColor = typedArray.getColor(R.styleable.TextItemLayout_til_bottom_line_color, getColorCompat(UI_R.color.divider_color))
            viewBinding.widgetTextItemLine.setBackgroundColor(bottomLineColor)
            val bottomLineAlignTitle = typedArray.getBoolean(R.styleable.TextItemLayout_til_bottom_line_align_title, false)
            val bottomLineHeight = typedArray.getDimension(R.styleable.TextItemLayout_til_bottom_line_height, 1F)
            adjustBottomLine(bottomLineAlignTitle, bottomLineHeight)
        } else {
            viewBinding.widgetTextItemLine.beGone()
        }
    }

    private fun adjustTitleToMiddle() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.connect(
            viewBinding.widgetTextItemTvTitle.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            getDimensionPixelSize(UI_R.dimen.common_page_edge)
        )
        constraintSet.connect(
            viewBinding.widgetTextItemTvTitle.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            getDimensionPixelSize(UI_R.dimen.common_page_edge)
        )
        constraintSet.applyTo(this)
    }

    private fun adjustBottomLine(bottomLineAlignTitle: Boolean, bottomLineHeight: Float) {
        (viewBinding.widgetTextItemLine.layoutParams as LayoutParams).height = bottomLineHeight.toInt()
        if (bottomLineAlignTitle) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(this)
            constraintSet.connect(viewBinding.widgetTextItemLine.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, dip(12))
            constraintSet.applyTo(this)
        }
    }

    var title: CharSequence?
        set(value) {
            viewBinding.widgetTextItemTvTitle.text = value
        }
        get() {
            return viewBinding.widgetTextItemTvTitle.textValue()
        }

    var subtitle: CharSequence?
        set(value) {
            viewBinding.widgetTextItemTvSubtitle.text = value
        }
        get() {
            return viewBinding.widgetTextItemTvSubtitle.textValue()
        }

}