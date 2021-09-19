@file:JvmName("Debug")

package com.app.base.debug

import android.content.Context
import android.content.Intent
import com.app.base.BuildConfig

fun isOpenDebug() = BuildConfig.openDebug

fun isOpenLog(): Boolean = BuildConfig.openLog

fun showDebugTools() = BuildConfig.showDebugTools

fun trustHttpsCertification() = BuildConfig.trustHttpsCertification

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
    val intent = Intent("com.sys.app.intent.Debug")
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

fun relaunchApp(context: Context) {
    val intent = Intent("com.android.app.base.AppLauncherActivity")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}