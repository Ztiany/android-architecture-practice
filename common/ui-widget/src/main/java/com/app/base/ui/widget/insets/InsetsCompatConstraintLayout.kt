package com.app.base.ui.widget.insets

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class InsetsCompatConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val insetsCompatHelper = InsetsCompatHelper(context, attrs)

    init {
        insetsCompatHelper.updateOriginalPadding(this)
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        insetsCompatHelper.applyWindowInsets(this)
    }

    override fun dispatchDraw(canvas: Canvas) {
        insetsCompatHelper.drawableInsetsColor(canvas, this)
        super.dispatchDraw(canvas)
    }

}