package com.android.base.ui.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.use
import com.android.base.ui.R

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 */
class TriangleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    @Suppress var bottomColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var strokeColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    @Suppress var triangleSolidColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var strokeWidth: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    @TriangleDirection var direction: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var trianglePercent: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    private val mTrianglePath: Path = Path()

    init {
        initAttribute(context, attrs)
    }

    private fun initAttribute(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.TriangleView).use {
            strokeColor = it.getColor(R.styleable.TriangleView_tv_triangle_stroke_color, Color.BLACK)
            bottomColor = it.getColor(R.styleable.TriangleView_tv_triangle_bottom_color, -10)
            if (bottomColor == -10) {
                bottomColor = strokeColor
            }
            triangleSolidColor = it.getColor(R.styleable.TriangleView_tv_triangle_solid_color, Color.TRANSPARENT)
            strokeWidth = it.getDimension(R.styleable.TriangleView_tv_triangle_stroke_width, 0F)
            direction = it.getInt(R.styleable.TriangleView_tv_triangle_direction, TriangleDirection.TOP)
            trianglePercent = it.getFloat(R.styleable.TriangleView_tv_triangle_angle_percent, 0.5F)
        }

        paint.strokeWidth = strokeWidth
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (measuredHeight <= 0 || measuredWidth <= 0) {
            return
        }
        when (direction) {
            TriangleDirection.TOP -> drawTopTriangle(canvas)
            TriangleDirection.BOTTOM -> drawBottomTriangle(canvas)
            TriangleDirection.LEFT -> drawLeftTriangle(canvas)
            TriangleDirection.RIGHT -> drawRightTriangle(canvas)
        }
    }

    private fun drawLeftTriangle(canvas: Canvas) {
        val trianglePoint = trianglePercent * measuredHeight
        val drawingHeight = measuredHeight.toFloat()
        val drawingWidth = measuredWidth.toFloat()
        val haftStroke = strokeWidth * 0.5F

        //draw solid
        if (needDrawSolid()) {
            mTrianglePath.reset()
            mTrianglePath.moveTo(drawingWidth, 0F)
            mTrianglePath.lineTo(drawingWidth, drawingHeight)
            mTrianglePath.lineTo(0F, trianglePoint)
            mTrianglePath.close()
            paint.style = Paint.Style.FILL
            paint.color = triangleSolidColor
            canvas.drawPath(mTrianglePath, paint)
        }
        //draw stroke
        if (strokeWidth != 0F) {
            paint.color = strokeColor
            canvas.drawLine(0F, trianglePoint, drawingWidth, 0F, paint)
            canvas.drawLine(0F, trianglePoint, drawingWidth, drawingHeight, paint)
            paint.color = bottomColor
            canvas.drawLine(drawingWidth - haftStroke, 0F, drawingWidth - haftStroke, drawingHeight, paint)
        }
    }

    private fun drawRightTriangle(canvas: Canvas) {
        val trianglePoint = trianglePercent * measuredHeight
        val drawingHeight = measuredHeight.toFloat()
        val drawingWidth = measuredWidth.toFloat()
        val haftStroke = strokeWidth * 0.5F

        //draw solid
        if (needDrawSolid()) {
            mTrianglePath.reset()
            mTrianglePath.moveTo(0F, 0F)
            mTrianglePath.lineTo(0F, drawingHeight)
            mTrianglePath.lineTo(drawingWidth, trianglePoint)
            mTrianglePath.close()
            paint.style = Paint.Style.FILL
            paint.color = triangleSolidColor
            canvas.drawPath(mTrianglePath, paint)
        }
        //draw stroke
        if (strokeWidth != 0F) {
            paint.color = strokeColor
            canvas.drawLine(0F, 0F, drawingWidth, trianglePoint, paint)
            canvas.drawLine(drawingWidth, trianglePoint, 0F, drawingHeight, paint)
            paint.color = bottomColor
            canvas.drawLine(haftStroke, 0F, haftStroke, drawingHeight, paint)
        }
    }

    private fun drawBottomTriangle(canvas: Canvas) {
        val trianglePoint = trianglePercent * measuredWidth
        val drawingHeight = measuredHeight.toFloat()
        val drawingWidth = measuredWidth.toFloat()
        val haftStroke = strokeWidth * 0.5F

        //draw solid
        if (needDrawSolid()) {
            mTrianglePath.reset()
            mTrianglePath.moveTo(0F, 0F)
            mTrianglePath.lineTo(trianglePoint, drawingHeight)
            mTrianglePath.lineTo(drawingWidth, 0F)
            mTrianglePath.close()
            paint.style = Paint.Style.FILL
            paint.color = triangleSolidColor
            canvas.drawPath(mTrianglePath, paint)
        }
        //draw stroke
        if (strokeWidth != 0F) {
            paint.color = strokeColor
            canvas.drawLine(0F, 0F, trianglePoint, drawingHeight, paint)
            canvas.drawLine(trianglePoint, drawingHeight, drawingWidth, 0F, paint)
            paint.color = bottomColor
            canvas.drawLine(0F, haftStroke, drawingWidth, haftStroke, paint)
        }
    }

    private fun drawTopTriangle(canvas: Canvas) {
        val trianglePoint = trianglePercent * measuredWidth
        val drawingHeight = measuredHeight.toFloat()
        val drawingWidth = measuredWidth.toFloat()
        val haftStroke = strokeWidth * 0.5F

        //draw solid
        if (needDrawSolid()) {
            mTrianglePath.reset()
            mTrianglePath.moveTo(0F, drawingHeight)
            mTrianglePath.lineTo(drawingWidth, drawingHeight)
            mTrianglePath.lineTo(trianglePoint, 0F)
            mTrianglePath.close()
            paint.style = Paint.Style.FILL
            paint.color = triangleSolidColor
            canvas.drawPath(mTrianglePath, paint)
        }
        //draw stroke
        if (strokeWidth != 0F) {
            paint.color = strokeColor
            canvas.drawLine(0F, drawingHeight, trianglePoint, 0F, paint)
            canvas.drawLine(trianglePoint, 0F, drawingWidth, drawingHeight, paint)
            paint.color = bottomColor
            canvas.drawLine(drawingWidth, drawingHeight - haftStroke, 0F, drawingHeight - haftStroke, paint)
        }
    }

    private fun needDrawSolid() = triangleSolidColor != Color.TRANSPARENT

}

@IntDef(TriangleDirection.TOP, TriangleDirection.BOTTOM, TriangleDirection.LEFT, TriangleDirection.RIGHT)
@Retention(AnnotationRetention.SOURCE)
annotation class TriangleDirection {
    companion object {
        const val TOP = 1
        const val BOTTOM = 2
        const val LEFT = 3
        const val RIGHT = 4
    }
}