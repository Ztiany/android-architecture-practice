package com.app.base.data.protocol

import android.os.Build
import com.app.base.BuildConfig
import com.app.base.app.Platform
import com.app.common.api.apiinterceptor.ApiInterceptor
import com.app.common.api.usermanager.UserManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Provider

internal class ProtocolInterceptor(
    private val userManager: UserManager,
    private val platform: Platform,
    private val apiInterceptor: Map<String, @JvmSuppressWildcards Provider<ApiInterceptor>>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newBuilder = chain.request().newBuilder()
        newBuilder.addHeader(HEADER_PROJECT_TOKEN_NAME, BuildConfig.apiKeyJoke)
        newBuilder.addHeader(HEADER_LOGIN_TOKEN_NAME, "")
        newBuilder.addHeader(HEADER_DEVICE_ID_NAME, platform.getDeviceId())
        newBuilder.addHeader(HEADER_CHANNEL_NAME, HEADER_CHANNEL_VALUE)
        newBuilder.addHeader(HEADER_APP_INFO_NAME, assembleAppInfo())
        newBuilder.addHeader(HEADER_DEVICE_INFO_NAME, assembleDeviceInfo())
        return chain.proceed(newBuilder.build())
    }

    private fun assembleDeviceInfo(): String {
        return listOf(
            Build.BRAND,
            Build.MODEL
        ).joinToString(separator = ";")
    }

    private fun assembleAppInfo(): String {
        return listOf(
            platform.getAppVersionName(),
            platform.getAppVersionNumber().toString(),
            Build.VERSION.SDK_INT
        ).joinToString(separator = ";")
    }

    companion object {
        private const val HEADER_PROJECT_TOKEN_NAME = "project_token"
        private const val HEADER_LOGIN_TOKEN_NAME = "token"
        private const val HEADER_DEVICE_ID_NAME = "uk"
        private const val HEADER_CHANNEL_NAME = "channel"
        private const val HEADER_CHANNEL_VALUE = "cretin_open_api"
        private const val HEADER_APP_INFO_NAME = "app"
        private const val HEADER_DEVICE_INFO_NAME = "device"
    }

}