package com.app.base.widget.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.android.base.utils.android.views.getColorCompat
import com.app.base.R

/**
 * only work for vertical orientation.
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-01-08 10:46
 */
class ColorDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private var top: Int = 1
    private var left: Int = 1
    private var right: Int = 1
    private var bottom: Int = 1

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColorCompat(R.color.divider_color)
    }

    private var drawLeft = false
    private var drawRight = false
    private var drawTop = false
    private var drawBottom = false

    private var skipStart = 0
    private var skipEnd = 0

    fun setDraw(drawLeft: Boolean = false, drawTop: Boolean = false, drawRight: Boolean = false, drawBottom: Boolean = false) {
        this.drawLeft = drawLeft
        this.drawBottom = drawBottom
        this.drawRight = drawRight
        this.drawTop = drawTop
    }

    fun setDrawWidth(left: Int = 1, top: Int = 1, right: Int = 1, bottom: Int = 1) {
        this.left = left
        this.bottom = bottom
        this.right = right
        this.top = top
    }

    fun setColor(@ColorInt color: Int) {
        paint.color = color
    }

    fun setSkipCount(start: Int, end: Int) {
        skipStart = start
        skipEnd = end
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager ?: return

        val end = layoutManager.itemCount - skipEnd

        val childCount = parent.childCount
        var childAdapterPosition: Int
        for (i in 0 until childCount) {
            childAdapterPosition = parent.getChildAdapterPosition(parent.getChildAt(i))
            if (childAdapterPosition in skipStart until end) {
                outRect.top = top
                outRect.bottom = bottom
                outRect.left = left
                outRect.right = right
            }
        }
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager ?: return

        super.onDrawOver(canvas, parent, state)
        val childCount = parent.childCount
        var child: View
        var childAdapterPosition: Int
        val end = layoutManager.itemCount - skipEnd

        for (i in 0 until childCount) {
            childAdapterPosition = parent.getChildAdapterPosition(parent.getChildAt(i))
            if (childAdapterPosition in skipStart until end) {
                child = parent.getChildAt(i)
                if (drawLeft && left > 0) {
                    canvas.drawRect(child.left - left.toFloat(), child.top.toFloat(), child.left.toFloat(), child.bottom.toFloat(), paint)
                }
                if (drawRight && right > 0) {
                    canvas.drawRect(child.right.toFloat(), child.top.toFloat(), child.right + right.toFloat(), child.bottom.toFloat(), paint)
                }
                if (drawTop && top > 0) {
                    canvas.drawRect(child.left.toFloat(), child.top - top.toFloat(), child.right.toFloat(), child.top.toFloat(), paint)
                }
                if (drawBottom && bottom > 0) {
                    canvas.drawRect(child.left.toFloat(), child.bottom.toFloat(), child.right.toFloat(), child.bottom + bottom.toFloat(), paint)
                }
            }
        }
    }

}