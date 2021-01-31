package com.app.base.data

import com.app.base.debug.Environment
import com.app.base.debug.EnvironmentContext

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-21 22:19
 */
internal fun addEnvironmentValues() = EnvironmentContext.startEdit {
    add(API_HOST, Environment("测试", "Test", "http://www.fake.com/api/"))
    add(H5_HOST, Environment("测试", "Test", "?"))
    add(WX_KEY, Environment("测试", "Test", "?"))
    add(ENV_VALUE, Environment("测试", "Test", "?"))

    add(API_HOST, Environment("生产", "Pro", "?"))
    add(H5_HOST, Environment("生产", "Pro", "?"))
    add(WX_KEY, Environment("生产", "Pro", "?"))
    add(ENV_VALUE, Environment("生产", "Pro", "?"))
}