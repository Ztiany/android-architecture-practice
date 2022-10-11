package me.ztiany.architecture.account.data

import com.android.common.api.protocol.UNAVAILABLE_FLAG

data class LoginResponse(
    val id: Long = UNAVAILABLE_FLAG.toLong(),
    val username: String = "",
    val token: String = "",
)