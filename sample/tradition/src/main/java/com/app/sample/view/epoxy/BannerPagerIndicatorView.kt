package com.app.sample.view.epoxy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.android.base.ui.banner.BannerViewPager
import com.android.base.ui.banner.IPagerNumberView
import com.android.base.utils.android.views.dip
import com.google.android.material.color.MaterialColors
import kotlin.math.roundToInt

class BannerPagerIndicatorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), IPagerNumberView {

    private var pageSize = 0
    private var currentPosition = 0

    private var circleRadius = 0F
    private var rectWidth = 0
    private var rectHeight = 0

    private val bgColor = MaterialColors.getColor(this, com.app.base.ui.theme.R.attr.app_color_deepest_opacity20)
    private val circleColor = MaterialColors.getColor(this, com.app.base.ui.theme.R.attr.app_color_lightest_opacity50)
    private val rectColor = MaterialColors.getColor(this, com.app.base.ui.theme.R.attr.app_color_lightest)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    override fun setBannerView(viewPager: BannerViewPager) = Unit

    override fun onPageScrolled(position: Int, positionOffset: Float) = Unit

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val circleRadius = dip(2)/*default*/
        val rectWidth = dip(12)/*default*/
        val rectHeight = circleRadius * 2
        val heightResult = paddingTop + paddingBottom + rectHeight
        val widthResult = paddingLeft + paddingRight + rectWidth * pageSize
        setMeasuredDimension(resolveSize(widthResult, widthMeasureSpec), resolveSize(heightResult, heightMeasureSpec))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (pageSize == 0) {
            return
        }
        rectWidth = ((w - paddingRight - paddingLeft) / pageSize.toFloat()).roundToInt()
        rectHeight = h - paddingTop - paddingBottom
        circleRadius = rectHeight / 2F
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = bgColor
        canvas.drawRoundRect(
            0F, 0F, width.toFloat(), height.toFloat(), rectHeight / 2F, rectHeight / 2F, paint
        )

        for (i in 0.rangeTo(pageSize)) {
            if (i == currentPosition) {
                drawableRect(canvas, i)
            } else {
                drawableCircle(canvas, i)
            }
        }
    }

    private fun drawableCircle(canvas: Canvas, position: Int) {
        paint.color = circleColor
        canvas.drawCircle(
            paddingLeft + position * rectWidth + rectWidth / 2F, paddingTop + rectHeight / 2F, circleRadius, paint
        )
    }

    private fun drawableRect(canvas: Canvas, position: Int) {
        paint.color = rectColor
        canvas.drawRoundRect(
            paddingLeft + position * rectWidth.toFloat(),
            paddingTop.toFloat(),
            paddingLeft + (position + 1) * rectWidth.toFloat(),
            paddingTop + rectHeight.toFloat(),
            rectHeight / 2F,
            rectHeight / 2F,
            paint
        )
    }

    override fun setPageSize(pageSize: Int) {
        this.pageSize = pageSize
        requestLayout()
    }

    override fun onPageSelected(position: Int) {
        currentPosition = position
        invalidate()
    }

}