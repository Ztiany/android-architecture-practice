package com.app.base.app

import android.content.Context
import android.os.Build
import com.android.base.utils.android.AppUtils
import com.android.base.utils.android.DeviceIdUtils
import com.android.base.utils.android.DeviceUtils
import com.android.base.utils.android.MetaDataUtils
import com.android.base.utils.android.network.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-03-22 15:39
 */
@Singleton
class AndroidPlatform @Inject internal constructor(
    @ApplicationContext private val context: Context
) {

    fun isConnected() = NetworkUtils.isConnected()

    fun getAppVersionName(): String = AppUtils.getAppVersionName()

    fun getManifestValue(key: String): String = MetaDataUtils.getMetaDataInApp(key)

    fun getDeviceId(): String = DeviceIdUtils.getDeviceId(context)

    fun getIpAddress(): String = NetworkUtils.getIPAddress(true)

    fun getMAC(): String = DeviceUtils.getMacAddress()

    /** 获取设备名 */
    fun getDeviceName(): String = Build.MODEL

}