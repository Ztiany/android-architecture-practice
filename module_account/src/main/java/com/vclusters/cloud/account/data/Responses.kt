package com.vclusters.cloud.account.data

import com.android.common.apispec.UNAVAILABLE_FLAG

data class LoginResponse(
    val id: Long = UNAVAILABLE_FLAG.toLong(),
    val username: String = "",
    val token: String = "",
    val nextCloudIP: String = "",
    val nextLocalNetworkIP: String = "",
)