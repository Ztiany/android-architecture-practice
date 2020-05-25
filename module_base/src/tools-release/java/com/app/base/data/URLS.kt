package com.app.base.data

import com.app.base.debug.Environment
import com.app.base.debug.EnvironmentContext

/*release包*/
internal fun addHost() = EnvironmentContext.startEdit {
    add(API_HOST, Environment("生产", "Pro", "https://xxx"))
    add(H5_HOST, Environment("生产", "Pro", "https://xxx"))
}