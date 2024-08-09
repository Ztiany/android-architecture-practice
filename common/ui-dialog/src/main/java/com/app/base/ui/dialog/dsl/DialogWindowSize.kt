package com.app.base.ui.dialog.dsl

import android.content.Context

@DialogContextDslMarker
class DialogWindowSize(
    private var _width: (context: Context) -> Int,
    private var _maxHeight: (context: Context) -> Int,
) {

    fun width(maxWidth: (context: Context) -> Int) {
        _width = maxWidth
    }

    fun maxHeight(maxHeight: (context: Context) -> Int) {
        _maxHeight = maxHeight
    }

    fun toDialogWindowSizeDescription(): DialogWindowSizeDescription {
        return DialogWindowSizeDescription(_width, _maxHeight)
    }

}

class DialogWindowSizeDescription(
    val width: (context: Context) -> Int,
    val maxHeight: (context: Context) -> Int,
) {

    fun getWidth(context: Context): Int {
        return width(context)
    }

    fun getMaxHeight(context: Context): Int {
        return maxHeight(context)
    }

}