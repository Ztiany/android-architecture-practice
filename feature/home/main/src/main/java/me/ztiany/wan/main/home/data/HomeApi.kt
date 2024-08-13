package me.ztiany.wan.main.home.data

import com.app.base.data.protocol.ApiResult
import retrofit2.http.POST

interface HomeApi {

    @POST("home/recommend")
    suspend fun recommends(): ApiResult<Unit>

}