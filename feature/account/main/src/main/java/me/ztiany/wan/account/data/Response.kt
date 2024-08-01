package me.ztiany.wan.account.data

data class LoginResponse(
    val isFirst: Int = 0,
    val id: Long = 0,
    val uid: String = "",
    val isCertified: Int = 0,
    val headImgUrl: String = "",
    val phoneNumber: String = "",
    val userName: String = "",
    val token: String = "",
)