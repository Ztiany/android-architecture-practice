package com.biyun.cg.box.account.data

import com.app.common.api.protocol.UNAVAILABLE_FLAG

data class LoginResponse(
    val id: Long = UNAVAILABLE_FLAG.toLong(),
    val username: String = "",
    val token: String = "",
)