package com.app.base.widget.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.use
import com.android.base.utils.android.views.*
import com.app.base.R
import kotlinx.android.synthetic.main.widget_text_item.view.*

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 */
class TextItemLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.widget_text_item, this)
        context.obtainStyledAttributes(attrs, R.styleable.TextItemLayout).use(::fillAttributes)
    }

    private fun fillAttributes(typedArray: TypedArray) {
        //title
        widgetTextItemTvTitle.text = typedArray.getString(R.styleable.TextItemLayout_til_title)
        typedArray.getColor(R.styleable.TextItemLayout_til_title_color, -1).let {
            if (it != -1) {
                widgetTextItemTvTitle.setTextColor(it)
            }
        }
        if (typedArray.getBoolean(R.styleable.TextItemLayout_til_title_in_middle, false)) {
            adjustTitleToMiddle()
        }
        //subtitle
        widgetTextItemTvSubtitle.text = typedArray.getString(R.styleable.TextItemLayout_til_subtitle)
        typedArray.getColor(R.styleable.TextItemLayout_til_subtitle_color, -1).let {
            if (it != -1) {
                widgetTextItemTvSubtitle.setTextColor(it)
            }
        }
        //arrow
        widgetTextItemIvArrow.visibleOrGone(typedArray.getBoolean(R.styleable.TextItemLayout_til_show_arrow, false))
        //line
        val visibleBottomLine = typedArray.getBoolean(R.styleable.TextItemLayout_til_show_bottom_line, false)
        if (visibleBottomLine) {
            widgetTextItemLine.visible()
            val bottomLineColor = typedArray.getColor(R.styleable.TextItemLayout_til_bottom_line_color, getColorCompat(R.color.divider_color))
            widgetTextItemLine.setBackgroundColor(bottomLineColor)
            val bottomLineAlignTitle = typedArray.getBoolean(R.styleable.TextItemLayout_til_bottom_line_align_title, false)
            val bottomLineHeight = typedArray.getDimension(R.styleable.TextItemLayout_til_bottom_line_height, 1F)
            adjustBottomLine(bottomLineAlignTitle, bottomLineHeight)
        } else {
            widgetTextItemLine.gone()
        }
    }

    private fun adjustTitleToMiddle() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)
        constraintSet.connect(widgetTextItemTvTitle.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, getDimensionPixelSize(R.dimen.common_page_edge))
        constraintSet.connect(widgetTextItemTvTitle.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, getDimensionPixelSize(R.dimen.common_page_edge))
        constraintSet.applyTo(this)
    }

    private fun adjustBottomLine(bottomLineAlignTitle: Boolean, bottomLineHeight: Float) {
        (widgetTextItemLine.layoutParams as LayoutParams).height = bottomLineHeight.toInt()
        if (bottomLineAlignTitle) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(this)
            constraintSet.connect(widgetTextItemLine.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, dip(12))
            constraintSet.applyTo(this)
        }
    }

    var title: CharSequence?
        set(value) {
            widgetTextItemTvTitle.text = value
        }
        get() {
            return widgetTextItemTvTitle.textValue()
        }

    var subtitle: CharSequence?
        set(value) {
            widgetTextItemTvSubtitle.text = value
        }
        get() {
            return widgetTextItemTvSubtitle.textValue()
        }

}