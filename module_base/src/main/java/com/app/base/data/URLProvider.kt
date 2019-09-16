package com.app.base.data

import com.gwchina.sdk.base.data.Environment
import com.gwchina.sdk.base.data.EnvironmentContext

private const val CATEGORY_HOST = "接口环境"
private const val H5_HOST = "H5环境"

internal fun getBaseUrl(): String {
    return EnvironmentContext.selected(CATEGORY_HOST).url
}

internal fun getBaseWebUrl(): String {
    return EnvironmentContext.selected(H5_HOST).url
}

internal fun addAllHost() {
    EnvironmentContext.startEdit {
        add(CATEGORY_HOST, Environment("测试", "https://www.baidu.com"))
        add(CATEGORY_HOST, Environment("压测", "https://www.baidu.com"))
        add(CATEGORY_HOST, Environment("开发", "https://www.baidu.com"))
        add(CATEGORY_HOST, Environment("正式", "https://www.baidu.com"))

        add(H5_HOST, Environment("测试", "https://www.baidu.com"))
        add(H5_HOST, Environment("压测", "https://www.baidu.com"))
        add(H5_HOST, Environment("开发", "https://www.baidu.com"))
        add(H5_HOST, Environment("本地)", "https://www.baidu.com"))
        add(H5_HOST, Environment("正式", "https://www.baidu.com"))
    }
}

internal fun addReleaseHost() {
    EnvironmentContext.startEdit {
        add(CATEGORY_HOST, Environment("正式", "https://www.baidu.com"))
        add(H5_HOST, Environment("正式", "https://www.baidu.com"))
    }
}