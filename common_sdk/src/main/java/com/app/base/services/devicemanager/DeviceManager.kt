package com.app.base.services.devicemanager

import kotlinx.coroutines.flow.Flow

interface DeviceManager {

    /** 获取所有云手机，[forceSync] 为 true 时表示强制从云端获取。*/
    suspend fun cloudDevices(forceSync: Boolean = false): List<CloudDevice>

    /** 订阅云手机 */
    fun flowableCloudDevices(): Flow<List<CloudDevice>>

}