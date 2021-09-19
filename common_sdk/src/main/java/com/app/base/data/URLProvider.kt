package com.app.base.data

import com.app.base.debug.EnvironmentContext

internal const val API_HOST = "接口环境"
internal const val H5_HOST = "H5环境"
internal const val WX_KEY = "微信KEY"
internal const val ENV_VALUE = "环境变量"

internal fun selectSpecified(specifiedHost: String) {
    EnvironmentContext.select(API_HOST, specifiedHost)
    EnvironmentContext.select(H5_HOST, specifiedHost)
    EnvironmentContext.select(WX_KEY, specifiedHost)
    EnvironmentContext.select(ENV_VALUE, specifiedHost)
}

internal fun getAPIBaseURL(): String {
    return EnvironmentContext.selected(API_HOST).value
}

internal fun getBaseWebURL(): String {
    return EnvironmentContext.selected(H5_HOST).value
}

fun getWxKey(): String {
    return EnvironmentContext.selected(WX_KEY).value
}

internal fun getEnvValuesPath(): String {
    return EnvironmentContext.selected(ENV_VALUE).value
}