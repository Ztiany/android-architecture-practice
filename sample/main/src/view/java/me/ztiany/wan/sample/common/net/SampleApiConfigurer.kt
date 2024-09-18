package me.ztiany.wan.sample.common.net

import com.android.base.utils.android.views.getString
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.provider.ApiHandler
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.ErrorMessage
import com.android.sdk.net.core.provider.HttpConfig
import com.app.apm.APM
import com.app.base.data.protocol.ApiResult
import com.app.base.utils.json.deserializeJson
import com.app.common.api.errorhandler.ErrorHandler
import com.blankj.utilcode.util.NetworkUtils
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

internal fun newErrorMessage(): ErrorMessage {
    return object : ErrorMessage {
        override fun netErrorMessage(exception: Throwable): CharSequence {
            if (NetworkUtils.isConnected()) {
                return getString(com.app.base.ui.theme.R.string.error_service_error)
            }
            return getString(com.app.base.ui.theme.R.string.error_net_error)
        }

        override fun serverDataErrorMessage(exception: Throwable): CharSequence {
            return getString(com.app.base.ui.theme.R.string.error_service_data_error)
        }

        override fun serverReturningNullEntityErrorMessage(exception: Throwable?): CharSequence {
            return getString(com.app.base.ui.theme.R.string.error_service_no_data_error)
        }

        override fun serverInternalErrorMessage(exception: Throwable): CharSequence {
            return getString(com.app.base.ui.theme.R.string.error_service_error)
        }

        override fun clientRequestErrorMessage(exception: Throwable): CharSequence {
            return getString(com.app.base.ui.theme.R.string.error_request_error)
        }

        override fun apiErrorMessage(exception: ApiErrorException): CharSequence {
            return getString(com.app.base.ui.theme.R.string.error_api_code_mask_tips, exception.code)
        }

        override fun unknownErrorMessage(exception: Throwable): CharSequence {
            return getString(com.app.base.ui.theme.R.string.error_unknown) + "：${exception.message}"
        }
    }
}

internal fun newApiHandler(errorHandler: ErrorHandler): ApiHandler = ApiHandler { result, hostFlag ->
    Timber.d("ApiHandler result: $result, hostFlag = $hostFlag")
    //登录状态已过期，请重新登录、账号在其他设备登陆
}