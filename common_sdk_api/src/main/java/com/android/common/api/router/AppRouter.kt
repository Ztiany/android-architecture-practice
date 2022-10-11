package com.android.common.api.router

/**
 * @author Ztiany
 * Date : 2017-11-04 13:44
 */
interface AppRouter {

    fun <T : Navigator> get(clazz: Class<T>): T?

    fun <T : Navigator> require(clazz: Class<T>): T

}