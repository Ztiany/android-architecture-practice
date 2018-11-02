package com.app.base.errorhandle

import com.app.base.data.net.ApiHelper

fun Throwable.isAppAuthenticationExpired() = ApiHelper.isAuthenticationExpired(this)
