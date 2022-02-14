package com.vclusters.cloud.main.home.assistant.data

import com.app.base.data.protocol.HttpResult
import retrofit2.http.GET

interface AssistantApi {

    @GET("user/v1/extend/getExtendServerList ")
    suspend fun loadAssistantFeatureConfig(): HttpResult<List<AssistantFeatureConfig>>

}