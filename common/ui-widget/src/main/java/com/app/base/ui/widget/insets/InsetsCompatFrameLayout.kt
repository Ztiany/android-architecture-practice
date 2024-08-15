package com.app.base.ui.widget.insets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class InsetsCompatFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val insetsCompatHelper = InsetsCompatHelper(context, attrs)

    init {
        insetsCompatHelper.updateOriginalPadding(this)
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        insetsCompatHelper.applyWindowInsets(this)
    }

}