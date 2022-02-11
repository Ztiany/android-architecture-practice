package com.app.base.services.devicemanager

interface DeviceManager {

    /** 获取所有云手机，[forceSync] 为 true 时表示强制从云端获取。*/
    suspend fun cloudDevices(forceSync: Boolean = false): List<CloudDevice>

    fun getCloudDeviceById(id: Int): CloudDevice?

}