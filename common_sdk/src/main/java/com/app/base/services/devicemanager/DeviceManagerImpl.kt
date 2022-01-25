package com.app.base.services.devicemanager

import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.android.sdk.net.extension.create
import com.app.base.app.ServiceProvider
import com.app.base.services.usermanager.UserManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DeviceManagerImpl @Inject constructor(
    private val serviceProvider: ServiceProvider,
    userManager: UserManager
) : DeviceManager {

    private val cloudDeviceApi by lazy {
        serviceProvider.getDefault().create<CloudDeviceApi>()
    }

    private var cloudDevices: List<CloudDevice> = emptyList()

    private val flowableCloudDevices = MutableStateFlow(cloudDevices)

    init {
        userManager.subscribeUser().onEach {

        }.launchIn(GlobalScope)
    }

    override suspend fun cloudDevices(forceSync: Boolean): List<CloudDevice> {
        val loaded = executeApiCall { cloudDeviceApi.loadCloudDevices() }
        updateCache(loaded.diskInfo)
        return loaded.diskInfo
    }

    private fun updateCache(diskInfo: List<CloudDeviceImpl>) {
        cloudDevices = diskInfo
        flowableCloudDevices.value = cloudDevices
    }

    override fun flowableCloudDevices(): Flow<List<CloudDevice>> = flowableCloudDevices

}