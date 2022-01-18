package com.vclusters.cloud.main.home.phone

import com.app.base.data.protocol.HttpResult
import retrofit2.http.GET

interface TestAPI {

    @GET("banner/json")
    suspend fun loadFirstKT(): HttpResult<List<Banner>?>

}

data class Banner(
    val desc: String,
    val id: Int
)