package com.app.apm


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