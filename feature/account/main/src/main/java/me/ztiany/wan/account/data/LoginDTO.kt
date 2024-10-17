package me.ztiany.wan.account.data

internal data class LoginRequest(
    val phoneNumber: String,
    val type: String,
    val code: String,
    val devicesId: String,
    val diskName: String,
    val versionNumber: Int,
    /** 固定值 */
    val clientResouce: Int = 10,
)

internal data class LoginResponse(
    val isFirst: Int = 0,
    val id: Long = 0,
    val uid: String = "",
    val isCertified: Int = 0,
    val headImgUrl: String = "",
    val phoneNumber: String = "",
    val userName: String = "",
    val token: String = "",
)