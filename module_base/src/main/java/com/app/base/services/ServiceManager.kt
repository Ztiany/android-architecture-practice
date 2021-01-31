package com.app.base.services

import com.android.base.concurrent.DispatcherProvider
import com.android.base.concurrent.SchedulerProvider
import com.android.sdk.net.core.service.ServiceFactory
import com.app.base.services.compression.ImageCompressionService
import com.app.base.services.compression.ImageCompressionServiceImpl

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-07 16:03
 */
class ServiceManager internal constructor(
        private val serviceFactory: ServiceFactory,
        private val schedulerProvider: SchedulerProvider,
        private val dispatcherProvider: DispatcherProvider
) {

    fun newImageCompressionService(): ImageCompressionService {
        return ImageCompressionServiceImpl(dispatcherProvider)
    }

}