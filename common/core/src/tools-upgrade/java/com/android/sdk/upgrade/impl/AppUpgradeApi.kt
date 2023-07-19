package com.android.sdk.upgrade.impl

import com.app.base.data.protocol.HttpResult
import retrofit2.http.GET
import retrofit2.http.Query

internal interface AppUpgradeApi {

    @GET("client/upgrade/loginFreeUpgradeNew")
    suspend fun loadNewVersionInfo(
        @Query("packageName") packageName:String,
        @Query("versionNumber") versionNumber:String,
    ): HttpResult<UpgradeResponse>

}