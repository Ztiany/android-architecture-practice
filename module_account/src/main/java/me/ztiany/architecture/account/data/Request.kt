package me.ztiany.architecture.account.data

data class LoginRequest(
    val phone: String,
    val password: String
)