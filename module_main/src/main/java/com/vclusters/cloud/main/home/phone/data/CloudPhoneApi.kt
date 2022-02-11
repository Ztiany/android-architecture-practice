package com.vclusters.cloud.main.home.phone.data

import com.app.base.data.protocol.HttpResult
import retrofit2.http.GET
import retrofit2.http.Query

interface CloudPhoneApi {

    @GET("public/v1/announce/homePublish")
    suspend fun loadHomeAnnouncements(): HttpResult<HomeAnnouncements>

    @GET("public/v1/announce/count")
    suspend fun queryMessageCount(): HttpResult<Int>

    @GET("api/wsi/v1/config/reset")
    suspend fun resetCloudDevice(@Query("userCardId") userCardId: String): HttpResult<Unit>

    @GET("api/wsi/v1/config/reboot")
    suspend fun rebootCloudDevice(@Query("userCardId") userCardId: String): HttpResult<Unit>

}