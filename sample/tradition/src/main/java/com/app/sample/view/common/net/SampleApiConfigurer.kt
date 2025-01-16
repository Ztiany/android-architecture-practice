package com.app.sample.view.common.net

import com.android.sdk.net.core.config.ErrorListener
import com.android.sdk.net.core.config.HttpConfig
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.app.apm.APM
import com.app.common.api.errorhandler.ErrorHandler
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

internal const val SAMPLE_HOST_FLAG = "SampleHostFlag"

internal fun newHttpConfig(): HttpConfig {

    return object : HttpConfig {

        override fun configRetrofit(builder: Retrofit.Builder) {
            builder.baseUrl("https://www.wanandroid.com/")
                .addConverterFactory(GsonConverterFactory.create(Gson()))
        }

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

internal fun newErrorListener(errorHandler: ErrorHandler) = object : ErrorListener {
    override fun onApiException(apiErrorException: ApiErrorException, hostFlag: String) {
        Timber.w("ApiHandler exception: $apiErrorException, hostFlag = $hostFlag")
        errorHandler.handleGlobalError(apiErrorException)
    }

    override fun onParsingDataFailed(exception: ServerErrorException, hostFlag: String) {
        Timber.w("onServerDataParseError exception: $exception, hostFlag = $hostFlag}")
    }

    override fun onDataNotReturned(exception: ServerErrorException, hostFlag: String) {
        Timber.w("onServerDataEmptyError exception: $exception, hostFlag = $hostFlag")
    }
}