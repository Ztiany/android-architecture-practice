package me.ztiany.architecture.account.data

import com.app.base.data.protocol.HttpResult
import retrofit2.http.Body
import retrofit2.http.POST


interface AccountApi {

    @POST("user/v1/client/login")
    suspend fun login(@Body loginRequest: LoginRequest): HttpResult<LoginResponse>

}