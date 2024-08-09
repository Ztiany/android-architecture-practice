package com.app.base.ui.dialog.dsl

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(Direction.TOP, Direction.BOTTOM, Direction.LEFT, Direction.RIGHT)
annotation class Direction {
    companion object {
        const val TOP = 1
        const val BOTTOM = 2
        const val LEFT = 3
        const val RIGHT = 4
    }
}