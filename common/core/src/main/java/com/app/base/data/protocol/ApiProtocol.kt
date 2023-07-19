package com.app.base.data.protocol

import com.app.base.app.AndroidPlatform
import com.app.base.config.AppSettings
import com.app.common.api.usermanager.UserManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response


/**
 * @author Ztiany
 */
fun configApiProtocol(
    userManager: UserManager,
    androidPlatform: AndroidPlatform,
    appSettings: AppSettings,
    builder: OkHttpClient.Builder
) {
    builder.addInterceptor(ApiInterceptor(userManager, androidPlatform, appSettings))
}

private class ApiInterceptor(
    private val userManager: UserManager,
    private val androidPlatform: AndroidPlatform,
    private val appSettings: AppSettings
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        ProtocolUtils.processHeader(userManager, androidPlatform, appSettings, newBuilder)
        return chain.proceed(newBuilder.build())
    }

}