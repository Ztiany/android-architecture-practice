package com.app.base.services.devicemanager

import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.android.sdk.net.extension.create
import com.app.base.app.DispatcherProvider
import com.app.base.app.ServiceProvider
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DeviceManagerImpl @Inject constructor(
    private val serviceProvider: ServiceProvider,
    private val dispatcherProvider: DispatcherProvider
) : DeviceManager {

    private val cloudDeviceApi by lazy {
        serviceProvider.getDefault().create<CloudDeviceApi>()
    }

    private var cloudDevices: List<CloudDevice> = emptyList()

    private val mutex = Mutex()

    override suspend fun cloudDevices(forceSync: Boolean): List<CloudDevice> {
        if (forceSync || cloudDevices.isEmpty()) {
            val loaded = withContext(dispatcherProvider.io()) {
                executeApiCall { cloudDeviceApi.loadCloudDevices() }
            }
            mutex.withLock {
                updateCache(loaded.diskInfo)
            }
        }

        mutex.withLock {
            return cloudDevices
        }
    }

    override fun getCloudDeviceById(id: Int): CloudDevice? {
        return cloudDevices.find { it.id == id }
    }

    private fun updateCache(diskInfo: List<CloudDeviceImpl>) {
        cloudDevices = diskInfo
    }

}