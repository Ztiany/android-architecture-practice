package me.ztiany.wan.###template###.data

import com.app.base.data.protocol.ApiResult
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

internal interface $$$template$$$Api {

    @POST("###template###")
    suspend fun ###template###(@Body request: $$$template$$$Request): ApiResult<$$$template$$$>

}