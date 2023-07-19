package com.app.base.widget.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.app.base.R
import com.google.android.material.color.MaterialColors

/**
 * Only work for vertical orientation.
 *
 * @author Ztiany
 */
class ColorDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private var topWidth: Int = 1
    private var leftWidth: Int = 1
    private var rightWidth: Int = 1
    private var bottomWidth: Int = 1

    var offsetChildForDivider = false

    private var topMargin: Int = 0
    private var leftMargin: Int = 0
    private var rightMargin: Int = 0
    private var bottomMargin: Int = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = MaterialColors.getColor(context, R.attr.app_color_divider, "app_color_divider not provided.")
    }

    private var drawLeft = false
    private var drawRight = false
    private var drawTop = false
    private var drawBottom = false

    private var skipStart = 0
    private var skipEnd = 0

    fun setEdges(drawLeft: Boolean = false, drawTop: Boolean = false, drawRight: Boolean = false, drawBottom: Boolean = false) {
        this.drawLeft = drawLeft
        this.drawBottom = drawBottom
        this.drawRight = drawRight
        this.drawTop = drawTop
    }

    fun setEdgesWidth(left: Int = 1, top: Int = 1, right: Int = 1, bottom: Int = 1) {
        this.leftWidth = left
        this.bottomWidth = bottom
        this.rightWidth = right
        this.topWidth = top
    }

    fun setEdgesMargins(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        this.leftMargin = left
        this.bottomMargin = bottom
        this.rightMargin = right
        this.topMargin = top
    }

    fun setDividerColor(@ColorInt color: Int) {
        paint.color = color
    }

    fun setSkipCount(start: Int, end: Int) {
        skipStart = start
        skipEnd = end
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (!offsetChildForDivider) {
            return
        }
        val layoutManager = parent.layoutManager ?: return

        val end = layoutManager.itemCount - skipEnd

        val childCount = parent.childCount
        var childAdapterPosition: Int
        for (i in 0 until childCount) {
            childAdapterPosition = parent.getChildAdapterPosition(parent.getChildAt(i))
            if (childAdapterPosition in skipStart until end) {
                outRect.top = topWidth
                outRect.bottom = bottomWidth
                outRect.left = leftWidth
                outRect.right = rightWidth
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

        if (offsetChildForDivider) {
            for (i in 0 until childCount) {
                childAdapterPosition = parent.getChildAdapterPosition(parent.getChildAt(i))
                if (childAdapterPosition in skipStart until end) {
                    child = parent.getChildAt(i)
                    if (drawLeft && leftWidth > 0) {
                        canvas.drawRect(
                            child.left.toFloat() - leftWidth.toFloat() + leftMargin,
                            child.top.toFloat() - topWidth.toFloat() + topMargin,
                            child.left.toFloat() + leftMargin,
                            child.bottom.toFloat() + bottomWidth.toFloat() - bottomMargin,
                            paint
                        )
                    }
                    if (drawRight && rightWidth > 0) {
                        canvas.drawRect(
                            child.right.toFloat() - rightMargin,
                            child.top.toFloat() - topWidth.toFloat() + topMargin,
                            child.right + rightWidth.toFloat() - rightMargin,
                            child.bottom.toFloat() + bottomWidth.toFloat() - bottomMargin,
                            paint
                        )
                    }
                    if (drawTop && topWidth > 0) {
                        canvas.drawRect(
                            child.left.toFloat() - leftWidth.toFloat() + leftMargin,
                            child.top - topWidth.toFloat() + topMargin,
                            child.right.toFloat() + rightWidth.toFloat() - rightMargin,
                            child.top.toFloat() + topMargin,
                            paint
                        )
                    }
                    if (drawBottom && bottomWidth > 0) {
                        canvas.drawRect(
                            child.left.toFloat() - leftWidth.toFloat() + leftMargin,
                            child.bottom.toFloat() - bottomMargin,
                            child.right.toFloat() + rightWidth.toFloat() - rightMargin,
                            child.bottom + bottomWidth.toFloat() - bottomMargin,
                            paint
                        )
                    }
                }
            }
        } else {
            for (i in 0 until childCount) {
                childAdapterPosition = parent.getChildAdapterPosition(parent.getChildAt(i))
                if (childAdapterPosition in skipStart until end) {
                    child = parent.getChildAt(i)
                    if (drawLeft && leftWidth > 0) {
                        canvas.drawRect(
                            child.left.toFloat() + leftMargin,
                            child.top.toFloat() - topWidth.toFloat() + topMargin,
                            child.left.toFloat() + leftMargin + leftWidth.toFloat(),
                            child.bottom.toFloat() + bottomWidth.toFloat() - bottomMargin,
                            paint
                        )
                    }
                    if (drawRight && rightWidth > 0) {
                        canvas.drawRect(
                            child.right.toFloat() - rightMargin - rightWidth.toFloat(),
                            child.top.toFloat() - topWidth.toFloat() + topMargin,
                            child.right.toFloat() - rightMargin,
                            child.bottom.toFloat() + bottomWidth.toFloat() - bottomMargin,
                            paint
                        )
                    }
                    if (drawTop && topWidth > 0) {
                        canvas.drawRect(
                            child.left.toFloat() - leftWidth.toFloat() + leftMargin,
                            child.top.toFloat() + topMargin,
                            child.right.toFloat() + rightWidth.toFloat() - rightMargin,
                            child.top.toFloat() + topMargin + topWidth.toFloat(),
                            paint
                        )
                    }
                    if (drawBottom && bottomWidth > 0) {
                        canvas.drawRect(
                            child.left.toFloat() - leftWidth.toFloat() + leftMargin,
                            child.bottom.toFloat() - bottomMargin - bottomWidth.toFloat(),
                            child.right.toFloat() + rightWidth.toFloat() - rightMargin,
                            child.bottom.toFloat() - bottomMargin,
                            paint
                        )
                    }
                }
            }
        }
    }

}