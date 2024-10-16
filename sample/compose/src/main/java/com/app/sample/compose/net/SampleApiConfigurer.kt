package com.app.sample.compose.net

import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.ErrorListener
import com.android.sdk.net.core.provider.HttpConfig
import com.app.apm.APM
import com.app.base.data.protocol.ApiResult
import com.app.base.utils.json.deserializeJson
import com.app.common.api.errorhandler.ErrorHandler
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit

internal const val SAMPLE_HOST_FLAG = "SampleHostFlag"

internal fun newHttpConfig(): HttpConfig {

    return object : HttpConfig {

        override fun baseUrl() = "https://www.wanandroid.com/"

        override fun configRetrofit(okHttpClient: OkHttpClient, builder: Retrofit.Builder) = false

        override fun configHttp(builder: OkHttpClient.Builder) {
            builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .apply {
                    APM.installOkHttpLogging(this) { message -> Timber.w("OkHttp: $message") }
                }
        }

    }

}

internal fun newErrorBodyParser(errorHandler: ErrorHandler): ErrorBodyParser {
    return object : ErrorBodyParser {
        override fun parseErrorBody(errorBody: String, hostFlag: String): ApiErrorException? {
            val errorResult = errorBody.deserializeJson(ApiResult::class.java)
            return if (errorResult == null) {
                null
            } else {
                val exception = ApiErrorException(errorResult.code, errorResult.message, hostFlag)
                errorHandler.handleGlobalError(exception)
                exception
            }
        }
    }
}

internal fun newErrorListener(errorHandler: ErrorHandler) = object : ErrorListener {
    override fun onApiErrorException(exception: ApiErrorException, hostFlag: String) {
        Timber.d("ApiHandler exception: $exception, hostFlag = $hostFlag")
        errorHandler.handleGlobalError(exception)
    }

    override fun onServerDataEmptyError(exception: ServerErrorException, hostFlag: String) {
        Timber.d("onServerDataEmptyError exception: $exception, hostFlag = $hostFlag")
        errorHandler.handleGlobalError(exception)
    }

    override fun onServerDataParseError(exception: ServerErrorException, hostFlag: String) {
        Timber.d("onServerDataParseError exception: $exception, hostFlag = $hostFlag}")
        errorHandler.handleGlobalError(exception)
    }
}