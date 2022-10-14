package com.android.base.ui.compat

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.R
import com.google.android.material.appbar.MaterialToolbar

/**
 * This Toolbar doesn't consume touch events.
 *
 *@author Ztiany
 */
class Toolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.style.Widget_MaterialComponents_Toolbar
) : MaterialToolbar(context, attrs, defStyleAttr) {

    var noHandleTouchEvent = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (noHandleTouchEvent) {
            return false
        }
        return super.onTouchEvent(ev)
    }

}