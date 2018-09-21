package com.app.base.data.net.errorhandler

fun Throwable.isAppAuthenticationExpired() = AppErrorHandler.isAuthenticationExpired(this)
