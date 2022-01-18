package com.vclusters.cloud.account.data

import com.android.common.apispec.ANDROID_CLIENT_FLAG


data class LoginRequest(
    val phone: String,
    val password: String,
    val diskName: String,
    val imei: String,
    val uuid: String,
    val mac: String,
    val ipAddr: String,
    val client: String = ANDROID_CLIENT_FLAG,
)