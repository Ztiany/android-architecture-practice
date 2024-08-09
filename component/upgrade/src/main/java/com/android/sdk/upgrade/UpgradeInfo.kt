package com.android.sdk.upgrade

/**
 *@author Ztiany
 */
data class UpgradeInfo(
    val isNewVersion: Boolean,
    val isForce: Boolean,
    val versionName: String,
    val downloadUrl: String,
    val title: String,
    val description: String,
    val digitalAbstract: String,
    val raw: Any?,
)