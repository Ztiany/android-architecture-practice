@file:JvmName("DispatcherProviders")

package com.app.common.api.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 * Providing different types of [CoroutineDispatcher]s.
 *
 *@author Ztiany
 */
interface DispatcherProvider {

    fun io(): CoroutineDispatcher

    fun computation(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher

}
