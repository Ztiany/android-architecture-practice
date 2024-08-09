package com.app.base.ui.dialog.dsl

import android.view.WindowManager
import android.widget.PopupWindow

class PopupWindowSize(
    private var _width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    private var _height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
) {

    fun width(width: Int) {
        _width = width
    }

    fun height(height: Int) {
        _height = height
    }

    fun size(width: Int, height: Int) {
        _width = width
        _height = height
    }

    fun wrapContent() {
        _width = WindowManager.LayoutParams.WRAP_CONTENT
        _height = WindowManager.LayoutParams.WRAP_CONTENT
    }

    fun fillMaxSize() {
        _width = WindowManager.LayoutParams.MATCH_PARENT
        _height = WindowManager.LayoutParams.MATCH_PARENT
    }

    fun fillMaxWidth() {
        _width = WindowManager.LayoutParams.MATCH_PARENT
    }

    fun fillMaxHeight() {
        _height = WindowManager.LayoutParams.MATCH_PARENT
    }

    fun toWindowSizeDescription(): PopupWindowSizeDescription {
        return PopupWindowSizeDescription(_width, _height)
    }

}

class PopupWindowSizeDescription(
    val width: Int,
    val height: Int,
)

fun PopupWindowSizeDescription.applyTo(popupWindow: PopupWindow) {
    popupWindow.width = width
    popupWindow.height = height
}