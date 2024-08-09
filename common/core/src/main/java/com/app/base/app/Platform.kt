package com.app.base.app

import android.content.Context
import android.os.Build
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.MetaDataUtils
import com.blankj.utilcode.util.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@author Ztiany
 */
@Singleton
class Platform @Inject internal constructor(@ApplicationContext private val context: Context) {

    fun isConnected() = NetworkUtils.isConnected()

    fun getAppVersionName(): String = AppUtils.getAppVersionName()

    fun getAppVersionNumber(): Int = AppUtils.getAppVersionCode()

    fun getManifestValue(key: String): String = MetaDataUtils.getMetaDataInApp(key)

    fun getDeviceId(): String = AndroidDeviceId.get(context)

    fun getIpAddress(): String = NetworkUtils.getIPAddress(true)

    fun getMAC(): String = DeviceUtils.getMacAddress()

    fun getPackageName(): String = AppUtils.getAppPackageName()

    fun getDeviceName(): String = Build.MODEL

    fun getAppChannel(): String {
        //val channel = ChannelReaderUtil.getChannel(context)
        //return if (channel.isNullOrEmpty()) "DEV" else channel
        return "DEV"
    }

}