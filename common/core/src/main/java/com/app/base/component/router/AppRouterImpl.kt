package com.app.base.component.router

import android.util.Log
import com.app.common.api.router.AppNavigator
import com.app.common.api.router.AppRouter
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * @author Ztiany
 */
@Singleton
internal class AppRouterImpl @Inject constructor(
    private val appRouterMap: Map<Class<out AppNavigator>, @JvmSuppressWildcards Provider<AppNavigator>>
) : AppRouter {

    init {
        Log.d("AppRouter", "AppRouterImpl initialized.")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : AppNavigator> getNavigator(clazz: Class<T>): T? {
        return appRouterMap[clazz]?.get() as? T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : AppNavigator> requireNavigator(clazz: Class<T>): T {
        return appRouterMap[clazz]?.get() as? T ?: throw NullPointerException("The navigator you required is not provided.")
    }

}