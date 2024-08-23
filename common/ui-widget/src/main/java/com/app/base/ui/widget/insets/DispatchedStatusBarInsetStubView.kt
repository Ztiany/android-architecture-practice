package com.app.base.ui.widget.insets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DispatchedStatusBarInsetStubView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var statusBarInsetHeight = 0

    init {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            if (statusBarInsetHeight != systemBars.top) {
                statusBarInsetHeight = systemBars.top
                requestLayout()
            }
            insets
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(statusBarInsetHeight, MeasureSpec.EXACTLY)
        )
    }

}