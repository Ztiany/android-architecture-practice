package me.ztiany.wan.account.data

import com.app.common.api.protocol.UNAVAILABLE_FLAG

data class LoginResponse(
    val isFirst: Int = 0,
    val id: Long = UNAVAILABLE_FLAG.toLong(),
    val uid: String = "",
    val isCertified: Int = 0,
    val headImgUrl: String = "",
    val phoneNumber: String = "",
    val userName: String = "",
    val token: String = ""
)