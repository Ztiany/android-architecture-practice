package com.gwchina.sdk.base.widget.shape

import android.support.annotation.IntDef
import com.gwchina.sdk.base.widget.shape.Direction.Companion.BOTTOM
import com.gwchina.sdk.base.widget.shape.Direction.Companion.LEFT
import com.gwchina.sdk.base.widget.shape.Direction.Companion.RIGHT
import com.gwchina.sdk.base.widget.shape.Direction.Companion.TOP

@IntDef(TOP, BOTTOM, LEFT, RIGHT)
@Retention(AnnotationRetention.SOURCE)
annotation class Direction {
    companion object {
        const val TOP = 1
        const val BOTTOM = 2
        const val LEFT = 3
        const val RIGHT = 4
    }
}