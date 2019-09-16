@file:JvmName("Debug")
package com.app.base.debug

import com.app.base.BuildConfig

fun isOpenDebug(): Boolean {
    return BuildConfig.openDebug
}

fun ifOpenDebug(action: () -> Unit) {
    if (isOpenDebug()) {
        action()
    }
}