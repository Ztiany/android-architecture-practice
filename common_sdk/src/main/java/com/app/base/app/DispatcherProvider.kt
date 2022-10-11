@file:JvmName("DispatcherProviders")

package com.app.base.app

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 * Allow providing different types of [CoroutineDispatcher]s.
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

internal class DefaultDispatcherProvider : DispatcherProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun computation(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }

}

