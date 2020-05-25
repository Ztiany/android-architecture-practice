package com.app.base.data

import com.app.base.debug.EnvironmentContext


internal const val API_HOST = "接口环境"
internal const val H5_HOST = "H5环境"

internal fun getAPIBaseURL(): String {
    return EnvironmentContext.selected(API_HOST).url
}

internal fun getBaseWebURL(): String {
    return EnvironmentContext.selected(H5_HOST).url
}