package com.app.base.data.protocol

import com.android.base.utils.android.views.getString
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.provider.ApiHandler
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.ErrorMessage
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.core.provider.PlatformInteractor
import com.app.base.app.AndroidPlatform
import com.app.base.config.AppSettings
import com.app.base.debug.DebugTools
import com.app.base.debug.ifOpenLog
import com.app.base.debug.isOpenDebug
import com.app.common.api.errorhandler.ErrorHandler
import com.app.common.api.usermanager.UserManager
import com.blankj.utilcode.util.NetworkUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.net.Proxy
import java.util.concurrent.TimeUnit
import com.app.base.ui.R as UI_R

internal fun newHttpConfig(
    userManager: UserManager,
    appSettings: AppSettings,
    androidPlatform: AndroidPlatform,
    errorHandler: ErrorHandler,
): HttpConfig {

    return object : HttpConfig {

        private val CONNECTION_TIME_OUT = 30
        private val IO_TIME_OUT = 30

        override fun baseUrl() = appSettings.baseApiUrl()

        override fun configRetrofit(
            okHttpClient: OkHttpClient,
            builder: Retrofit.Builder,
        ): Boolean {
            return false
        }

        override fun configHttp(builder: OkHttpClient.Builder) {
            //常规配置
            builder.connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
            //Api 签名协议
            // configApiProtocol(userManager, androidPlatform, appSettings, builder)
            //调试配置
            configDebugIfNeeded(builder)
        }

        private fun configDebugIfNeeded(builder: OkHttpClient.Builder) {
            //打印日志
            ifOpenLog {
                val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("===OkHttp===").i(message) }
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(httpLoggingInterceptor)
            }

            //Stetho 调试
            if (isOpenDebug()) {
                DebugTools.installStethoHttp(builder)
            } else {
                //禁用代理
                builder.proxy(Proxy.NO_PROXY)
            }

            builder.authenticator { _, _ -> //下面的 newApiHandler 中已经处理，这里不需要再处理了。
                //errorHandler.handleGlobalError(ApiHelper.buildAuthenticationExpiredException())
                null
            }

            //HTTPS
            //trustAllCertificationChecked(builder)
        }
    }

}

fun newPlatformInteractor(androidPlatform: AndroidPlatform): PlatformInteractor {
    return object : PlatformInteractor {
        override fun isConnected(): Boolean {
            return androidPlatform.isConnected()
        }
    }
}

internal fun newErrorBodyParser(errorHandler: ErrorHandler): ErrorBodyParser {
    return object : ErrorBodyParser {
        override fun parseErrorBody(errorBody: String, hostFlag: String): ApiErrorException? {
            return null
        }
    }
}

internal fun newErrorMessage(): ErrorMessage {
    return object : ErrorMessage {
        override fun netErrorMessage(exception: Throwable): CharSequence {
            if (NetworkUtils.isConnected()) {
                return getString(UI_R.string.error_service_error)
            }
            return getString(UI_R.string.error_net_error)
        }

        override fun serverDataErrorMessage(exception: Throwable): CharSequence {
            return getString(UI_R.string.error_service_data_error)
        }

        override fun serverReturningNullEntityErrorMessage(exception: Throwable?): CharSequence {
            return getString(UI_R.string.error_service_no_data_error)
        }

        override fun serverInternalErrorMessage(exception: Throwable): CharSequence {
            return getString(UI_R.string.error_service_error)
        }

        override fun clientRequestErrorMessage(exception: Throwable): CharSequence {
            return getString(UI_R.string.error_request_error)
        }

        override fun apiErrorMessage(exception: ApiErrorException): CharSequence {
            return getString(UI_R.string.error_api_code_mask_tips, exception.code)
        }

        override fun unknownErrorMessage(exception: Throwable): CharSequence {
            return getString(UI_R.string.error_unknown) + "：${exception.message}"
        }
    }
}

internal fun newApiHandler(errorHandler: ErrorHandler): ApiHandler = ApiHandler { result, hostFlag ->
    Timber.d("ApiHandler result: $result")
    //登录状态已过期，请重新登录、账号在其他设备登陆
    errorHandler.handleGlobalError(ApiErrorException(result.code, result.message, hostFlag))
}