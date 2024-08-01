package com.android.sdk.upgrade.impl

import com.app.base.data.protocol.ApiFlag

data class UpgradeResponse(
    val appId: Int = 0,
    val businessId: String = "",
    val createTime: String = "",
    val fileSize: String = "",
    val id: Int = 0,
    val isDel: Int = 0,
    val packageName: String = "",
    val updateStatus: Int = 0,
    val url: String = "",
    val versionDescription: String = "",
    val versionNumber: String = "",
    val versionType: Int = 0,
)

val UpgradeResponse.isForce: Boolean
    get() = updateStatus == ApiFlag.API_FLAG_POSITIVE
