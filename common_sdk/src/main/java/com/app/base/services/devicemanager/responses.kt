package com.app.base.services.devicemanager

internal data class CloudDevicesResponse(
    /** 无用字段 */
    val isTrial: Boolean = false,
    val diskInfo: List<CloudDeviceImpl> = emptyList()
)