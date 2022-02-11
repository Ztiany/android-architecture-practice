package com.app.base.services.devicemanager

import com.app.base.data.protocol.HttpResult
import retrofit2.http.GET

internal interface CloudDeviceApi {

    @GET("user/v1/client/disk/info")
    suspend fun loadCloudDevices(): HttpResult<CloudDevicesResponse>

}