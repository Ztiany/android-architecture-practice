package com.app.base.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response


/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-10-11 09:32
 */
fun configApiProtocol(builder: OkHttpClient.Builder) {
    builder.addInterceptor(ApiInterceptor())
}

private class ApiInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        ProtocolUtils.processHeader(newBuilder)
        return chain.proceed(newBuilder.build())
    }

}