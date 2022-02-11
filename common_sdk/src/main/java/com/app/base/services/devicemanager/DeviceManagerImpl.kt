package com.app.base.services.devicemanager

import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.android.sdk.net.extension.create
import com.app.base.app.ServiceProvider
import com.app.base.services.usermanager.UserManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TODO：Clear devices when user switched.
 */
@Singleton
internal class DeviceManagerImpl @Inject constructor(
    private val serviceProvider: ServiceProvider,
    private val userManager: UserManager
) : DeviceManager {

    private val cloudDeviceApi by lazy {
        serviceProvider.getDefault().create<CloudDeviceApi>()
    }

    private var cloudDevices: List<CloudDevice> = emptyList()

    override suspend fun cloudDevices(forceSync: Boolean): List<CloudDevice> {
        if (!forceSync && cloudDevices.isNotEmpty()) {
            return cloudDevices
        }

        val loaded = executeApiCall { cloudDeviceApi.loadCloudDevices() }
        updateCache(loaded.diskInfo)
        return loaded.diskInfo
    }

    override fun getCloudDeviceById(id: Int): CloudDevice? {
        return cloudDevices.find { it.id == id }
    }

    private fun updateCache(diskInfo: List<CloudDeviceImpl>) {
        cloudDevices = diskInfo
    }

}