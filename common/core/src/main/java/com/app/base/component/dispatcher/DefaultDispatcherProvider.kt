package com.app.base.component.dispatcher

import com.app.common.api.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultDispatcherProvider @Inject constructor() : DispatcherProvider {

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

