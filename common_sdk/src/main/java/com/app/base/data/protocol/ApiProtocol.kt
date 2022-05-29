package com.app.base.data.protocol

import com.app.base.app.AndroidPlatform
import com.app.base.component.usermanager.UserManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response


/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-10-11 09:32
 */
fun configApiProtocol(
    userManager: UserManager,
    androidPlatform: AndroidPlatform,
    builder: OkHttpClient.Builder
) {
    builder.addInterceptor(ApiInterceptor(userManager, androidPlatform))
}

private class ApiInterceptor(
    private val userManager: UserManager,
    private val androidPlatform: AndroidPlatform
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        ProtocolUtils.processHeader(userManager, androidPlatform, newBuilder)
        return chain.proceed(newBuilder.build())
    }

}