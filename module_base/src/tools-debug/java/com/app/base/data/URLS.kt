package com.app.base.data

import com.app.base.debug.Environment
import com.app.base.debug.EnvironmentContext

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-05-25 10:53
 */
/*debug包*/
internal fun addHost() = EnvironmentContext.startEdit {
    add(API_HOST, Environment("测试", "Test", "http://xxx/"))
    add(H5_HOST, Environment("测试", "Test", "http://xxx"))

    add(API_HOST, Environment("开发", "Dev", "http://xxx"))
    add(H5_HOST, Environment("开发", "Dev", "http://xxx"))

    add(API_HOST, Environment("生产", "Pro", "https://xxx"))
    add(H5_HOST, Environment("生产", "Pro", "https://xxx"))
}