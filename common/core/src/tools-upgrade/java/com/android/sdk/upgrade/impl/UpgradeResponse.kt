package com.android.sdk.upgrade.impl

import com.app.common.api.protocol.FLAG_POSITIVE

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
    val versionType: Int = 0
)

val UpgradeResponse.isForce: Boolean
    get() = updateStatus == FLAG_POSITIVE
