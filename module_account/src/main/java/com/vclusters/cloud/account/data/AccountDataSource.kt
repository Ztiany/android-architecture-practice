package com.vclusters.cloud.account.data

import com.android.sdk.net.coroutines.CallResult

interface AccountDataSource {

    suspend fun login(phone: String, password: String): CallResult<LoginResponse>

}