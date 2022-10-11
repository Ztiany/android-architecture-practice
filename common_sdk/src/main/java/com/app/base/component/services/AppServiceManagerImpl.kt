package com.app.base.component.services

import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import com.android.common.api.services.AppService
import com.android.common.api.services.AppServiceFactory
import com.android.common.api.services.AppServiceManager
import com.android.common.api.services.OnService
import timber.log.Timber

internal class AppServiceManagerImpl : AppServiceManager {

    private val services = hashMapOf<String, AppService>()
    private val serviceFactories = hashMapOf<String, AppServiceFactory>()
    private val onServices = hashMapOf<String, MutableList<OnService>>()

    @Synchronized
    override fun registerService(name: String, appService: AppService) {
        checkIfExisted(name)
        services[name] = appService
        tryNotifyServiceRegistered(name)
    }

    override fun registerService(name: String, factory: AppServiceFactory) {
        checkIfExisted(name)
        serviceFactories[name] = factory
        tryNotifyServiceRegistered(name)
    }

    private fun checkIfExisted(name: String) {
        if (services.containsKey(name) || serviceFactories.containsKey(name)) {
            Timber.w("the service named [$name] has already been registered. now replace it.")
        }
    }

    private fun tryNotifyServiceRegistered(name: String) {
        val service = tryGetService(name) ?: return
        onServices.remove(name)?.forEach {
            it(service)
        }
    }

    @Synchronized
    override fun unregisterService(name: String): Boolean {
        return services.remove(name) != null
    }

    @Synchronized
    override fun <T : AppService> getService(name: String): T? {
        @Suppress("UNCHECKED_CAST") return services[name] as? T
    }

    @Synchronized
    override fun <T : AppService> requireService(name: String): T {
        @Suppress("UNCHECKED_CAST") return (services[name] as? T)
            ?: throw NullPointerException("the service named [$name] has not been registered.")
    }

    @Synchronized
    override fun onService(name: String, action: OnService) {
        tryGetService(name).ifNonNull {
            action(this)
        } otherwise {
            onServices[name].ifNonNull {
                add(action)
            } otherwise {
                onServices.put(name, mutableListOf(action))
            }
        }
    }

    private fun tryGetService(name: String): AppService? {
        var service = services[name]
        if (service == null) {
            service = serviceFactories[name]?.create()
            if (service != null) {
                services[name] = service
                serviceFactories.remove(name)
            }
        }
        return service
    }

    @Synchronized
    override fun cancelOnService(name: String, action: OnService) {
        onServices[name]?.remove(action)
    }

}