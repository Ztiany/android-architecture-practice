package com.app.base.debug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object DebugInfoDispatcher {

    private val innerPushToken = MutableLiveData<String>()
    val pushToken: LiveData<String>
        get() = innerPushToken

    private val innerUmengDeviceInfo = MutableLiveData<String>()
    val umengDeviceInfo: LiveData<String>
        get() = innerUmengDeviceInfo

    fun dispatchPushToken(token: String) {
        innerPushToken.postValue(token)
    }

    fun dispatchUmengDeviceInfo(deviceInfo: String) {
        innerUmengDeviceInfo.postValue(deviceInfo)
    }

}