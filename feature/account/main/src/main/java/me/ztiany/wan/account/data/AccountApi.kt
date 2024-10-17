package me.ztiany.wan.account.data

import com.app.base.data.protocol.ApiResult
import retrofit2.http.Body
import retrofit2.http.POST


internal interface AccountApi {

    @POST("user/sms/code/send/sendSms/new")
    suspend fun sendLoginCode(@Body codeRequest: CodeRequest): ApiResult<Unit>

    @POST("client/user/operation/codeLogin")
    suspend fun codeLogin(@Body loginRequest: LoginRequest): ApiResult<LoginResponse>

}