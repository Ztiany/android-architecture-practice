@file:JvmName("DispatcherProviders")

package com.app.common.api.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 * Providing different types of [CoroutineDispatcher]s.
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-03-18 14:06
 */
interface DispatcherProvider {

    fun io(): CoroutineDispatcher

    fun computation(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher

}
