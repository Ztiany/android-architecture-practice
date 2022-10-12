package com.android.common.api.services

/** A class that manages all the app services. */
interface AppServiceManager {

    fun <T : AppService> getService(clazz: Class<T>): T?

    fun <T : AppService> requireService(clazz: Class<T>): T

}

inline fun <reified T : AppService> AppServiceManager.getService(): T? {
    return getService(T::class.java)
}

inline fun <reified T : AppService> AppServiceManager.requireService(): T {
    return requireService(T::class.java)
}

inline fun <reified T : AppService> AppServiceManager.withService(
    onLost: () -> Unit = {},
    onService: T.() -> Unit
) {
    val service = getService(T::class.java)
    if (service != null) {
        onService(service)
    } else {
        onLost()
    }
}