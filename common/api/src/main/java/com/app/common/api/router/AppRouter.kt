package com.app.common.api.router

/**
 * @author Ztiany
 * Date : 2017-11-04 13:44
 */
interface AppRouter {

    fun <T : Navigator> getNavigator(clazz: Class<T>): T?

    fun <T : Navigator> requireNavigator(clazz: Class<T>): T

}

inline fun <reified T : Navigator> AppRouter.getNavigator(): T? {
    return getNavigator(T::class.java)
}

inline fun <reified T : Navigator> AppRouter.requireNavigator(): T {
    return requireNavigator(T::class.java)
}

inline fun <reified T : Navigator> AppRouter.withNavigator(
    onLost: () -> Unit = {}, onService: T.() -> Unit
) {
    val service = getNavigator(T::class.java)
    if (service != null) {
        onService(service)
    } else {
        onLost()
    }
}