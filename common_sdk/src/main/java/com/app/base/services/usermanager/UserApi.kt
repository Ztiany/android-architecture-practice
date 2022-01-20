package com.app.base.services.usermanager

import com.app.base.data.protocol.HttpResult
import retrofit2.http.GET

interface UserApi {

    @GET("user/v1/client/personal_info")
    suspend fun loadUserInfo(): HttpResult<User>

}