package com.android.common.api.router

/**
 * @author Ztiany
 * Date : 2017-11-04 13:44
 */
interface AppRouter {

    fun <T : AppNavigator> getNavigator(clazz: Class<T>): T?

    fun <T : AppNavigator> requireNavigator(clazz: Class<T>): T

}

inline fun <reified T : AppNavigator> AppRouter.getNavigator(): T? {
    return getNavigator(T::class.java)
}

inline fun <reified T : AppNavigator> AppRouter.requireNavigator(): T {
    return requireNavigator(T::class.java)
}

inline fun <reified T : AppNavigator> AppRouter.withNavigator(
    onLost: () -> Unit = {}, onService: T.() -> Unit
) {
    val service = getNavigator(T::class.java)
    if (service != null) {
        onService(service)
    } else {
        onLost()
    }
}