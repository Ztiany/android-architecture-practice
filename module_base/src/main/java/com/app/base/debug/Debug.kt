@file:JvmName("Debug")
package com.app.base.debug

import android.content.Context
import android.content.Intent
import com.app.base.BuildConfig


fun isOpenDebug() = BuildConfig.openDebug

fun isOpenLog(): Boolean = BuildConfig.openLog

fun showDisturbingDebugTools() = BuildConfig.showDisturbingDebugTools

fun ifOpenLog(action: () -> Unit) {
    if (isOpenLog()) {
        action()
    }
}

fun ifOpenDebug(action: () -> Unit) {
    if (isOpenDebug()) {
        action()
    }
}

fun openAppDebugActivity(context: Context) {
    val intent = Intent("com.app.base.debug.intent.Debug")
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}