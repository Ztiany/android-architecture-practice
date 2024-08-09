package com.app.base.ui.dialog.dsl

import com.app.base.ui.dialog.annotation.IncrementalConfig

interface PopupWindowDescription

@DslMarker
annotation class PopupWindowDslMarker

@PopupWindowDslMarker
interface PopupWindowConfig<Description : PopupWindowDescription> {

    fun size(config: PopupWindowSize.() -> Unit)

    fun dimStyle(config: PopupDim.() -> Unit)

    @IncrementalConfig
    fun behavior(config: PopupWindowBehavior.() -> Unit)

    fun toDescription(): PopupWindowDescription

}

fun PopupWindowConfig<*>.autoDismiss(autoDismiss: Boolean) {
    behavior {
        isAutoDismiss(autoDismiss)
    }
}

fun PopupWindowConfig<*>.disableDim() {
    dimStyle { disable() }
}

fun PopupWindowConfig<*>.systemDim(percent: Float) {
    dimStyle { systemDim(percent) }
}

fun PopupWindowConfig<*>.size(width: Int, height: Int) {
    size {
        size(width, height)
    }
}