package me.ztiany.wan.account.data

import com.app.base.data.protocol.HttpResult
import retrofit2.http.Body
import retrofit2.http.POST


interface AccountApi {

    /* 返回  {"code":200,"success":true,"data":{},"msg":"验证码发送成功"}  */
    @POST("user/sms/code/send/sendSms/new")
    suspend fun sendLoginCode(@Body codeRequest: CodeRequest): HttpResult<Unit>

    @POST("client/user/operation/codeLogin")
    suspend fun codeLogin(@Body loginRequest: LoginRequest): HttpResult<LoginResponse>

}