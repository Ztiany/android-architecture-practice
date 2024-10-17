package com.app.apm

import com.tencent.bugly.crashreport.CrashReport


fun APM.doIfLogEnabled(action: () -> Unit) {
    if (logEnabled) {
        action()
    }
}

fun APM.doIfDebugMode(action: () -> Unit) {
    if (debugMode) {
        action()
    }
}

fun APM.reportException(ex: Throwable) {
    CrashReport.postCatchedException(ex)
}