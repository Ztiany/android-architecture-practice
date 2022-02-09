package com.vclusters.cloud.main.home.phone.data

import com.app.base.data.protocol.HttpResult
import retrofit2.http.GET

interface CloudPhoneApi {

    @GET("public/v1/announce/homePublish")
    suspend fun loadHomeAnnouncements(): HttpResult<HomeAnnouncements>

    @GET("public/v1/announce/count")
    suspend fun queryMessageCount(): HttpResult<Int>

}