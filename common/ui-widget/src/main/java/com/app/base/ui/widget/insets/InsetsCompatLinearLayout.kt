package com.app.base.ui.widget.insets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InsetsCompatLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private val insetsCompatHelper = InsetsCompatHelper(context, attrs)

    init {
        insetsCompatHelper.updateOriginalPadding(this)
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        insetsCompatHelper.applyWindowInsets(this)
    }

}