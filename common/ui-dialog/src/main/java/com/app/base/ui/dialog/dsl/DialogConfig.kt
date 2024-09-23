package com.app.base.ui.dialog.dsl

import com.app.base.ui.dialog.annotation.IncrementalConfig

interface DialogDescription

@DslMarker
annotation class DialogContextDslMarker

@DialogContextDslMarker
interface DialogConfig<Behavior : DialogBehavior, Description : DialogDescription> {

    @IncrementalConfig
    fun behavior(config: Behavior.() -> Unit)

    fun toDescription(): Description

}

fun DialogConfig<*, *>.onDismiss(onDismissListener: OnDialogDismiss) {
    behavior {
        onDismiss(onDismissListener)
    }
}

fun DialogConfig<*, *>.disableAutoDismiss() {
    behavior {
        autoDismiss(false)
    }
}

fun DialogConfig<*, *>.noCancelable() {
    behavior {
        cancelable(false)
    }
}

fun DialogConfig<*, *>.noCancelOnTouchOutside() {
    behavior {
        cancelOnTouchOutside(false)
    }
}