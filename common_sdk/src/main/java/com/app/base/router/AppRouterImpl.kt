package com.app.base.router

import com.android.common.router.Navigator
import java.util.*

/**
 * @author Ztiany
 */
internal class AppRouterImpl : AppRouter {

    override fun <T : Navigator> get(clazz: Class<T>): T? {
        return ServiceLoader.load(clazz).firstOrNull()
    }

    override fun <T : Navigator> require(clazz: Class<T>): T {
        return ServiceLoader.load(clazz).firstOrNull() ?: throw NullPointerException("implementation of $clazz not found.")
    }

}