package me.ztiany.widget.compat

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.R

/**
 * This Toolbar doesn't consume touch events.
 *
 *@author Ztiany
 */
class Toolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.toolbarStyle
) : androidx.appcompat.widget.Toolbar(context, attrs, defStyleAttr) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

}