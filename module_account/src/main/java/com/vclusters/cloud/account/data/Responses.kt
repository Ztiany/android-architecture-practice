package com.vclusters.cloud.account.data

import com.android.common.apispec.UNAVAILABLE_FLAG

data class LoginResponse(
    val token: String = "",
    val nextCloudIP: String = "",
    val nextLocalNetworkIP: String = "",
    val username: String = "",
    val id: Long = UNAVAILABLE_FLAG.toLong(),
)

