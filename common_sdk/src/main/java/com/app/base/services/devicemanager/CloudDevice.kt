package com.app.base.services.devicemanager

interface CloudDevice {
    val androidcardIp: String
    val androidcardPort: String
    val areaId: Int
    val areaName: String
    val areaStatus: Int
    val authPhone: Int
    val backupStatus: Int
    val businessPort: String
    val buyVipType: String
    val cardId: Int
    val ctime: String
    val diskName: String
    val exIp: String
    val exPort: String
    val exceptTime: String
    val firstConnectCloudPhone: Boolean
    val id: Int
    val myOrderNum: String
    val overStatus: Int
    val overdue: Boolean
    val partitionId: Int
    val phoneAuthStatus: Int
    val previewImageId: Int
    val rbdExtendSize: Int
    val readme: String
    val room: String
    val screenShareStatus: Int
    val sn: String
    val snapRecoveryStatus: Int
    val username: String
    val validTime: Int
}

internal data class CloudDeviceImpl(
    override val androidcardIp: String = "",
    override val androidcardPort: String = "",
    override val areaId: Int = 0,
    override val areaName: String = "",
    override val areaStatus: Int = 0,
    override val authPhone: Int = 0,
    override val backupStatus: Int = 0,
    override val businessPort: String = "",
    override val buyVipType: String = "",
    override val cardId: Int = 0,
    override val ctime: String = "",
    override val diskName: String = "",
    override val exIp: String = "",
    override val exPort: String = "",
    override val exceptTime: String = "",
    override val firstConnectCloudPhone: Boolean = false,
    override val id: Int = 0,
    override val myOrderNum: String = "",
    override val overStatus: Int = 0,
    override val overdue: Boolean = false,
    override val partitionId: Int = 0,
    override val phoneAuthStatus: Int = 0,
    override val previewImageId: Int = 0,
    override val rbdExtendSize: Int = 0,
    override val readme: String = "",
    override val room: String = "",
    override val screenShareStatus: Int = 0,
    override val sn: String = "",
    override val snapRecoveryStatus: Int = 0,
    override val username: String = "",
    override val validTime: Int = 0
) : CloudDevice


