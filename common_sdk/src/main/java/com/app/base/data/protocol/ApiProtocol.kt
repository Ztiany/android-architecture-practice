package com.app.base.data.protocol

import com.app.base.services.usermanager.UserManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response


/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-10-11 09:32
 */
fun configApiProtocol(userManager: UserManager, builder: OkHttpClient.Builder) {
    builder.addInterceptor(ApiInterceptor(userManager))
}

private class ApiInterceptor(private val userManager: UserManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        ProtocolUtils.processHeader(userManager, newBuilder)
        return chain.proceed(newBuilder.build())
    }

}