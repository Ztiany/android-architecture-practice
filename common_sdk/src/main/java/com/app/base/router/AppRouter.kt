package com.app.base.router

import com.android.common.router.Navigator

/**
 * @author Ztiany
 * Date : 2017-11-04 13:44
 */
interface AppRouter {

    fun <T : Navigator> get(clazz: Class<T>): T?

    fun <T : Navigator> require(clazz: Class<T>): T

}