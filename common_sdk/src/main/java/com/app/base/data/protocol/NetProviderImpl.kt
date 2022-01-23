package com.app.base.data.protocol

import com.android.base.utils.android.views.getString
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.provider.*
import com.app.base.R
import com.app.base.app.AndroidPlatform
import com.app.base.app.ErrorHandler
import com.app.base.config.AppSettings
import com.app.base.debug.DebugTools
import com.app.base.debug.ifOpenLog
import com.app.base.debug.isOpenDebug
import com.app.base.services.usermanager.UserManager
import com.app.base.utils.JsonUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.net.Proxy
import java.util.concurrent.TimeUnit

internal fun newHttpConfig(
    userManager: UserManager,
    appSettings: AppSettings,
    androidPlatform: AndroidPlatform,
    errorHandler: ErrorHandler
): HttpConfig {

    return object : HttpConfig {

        private val CONNECTION_TIME_OUT = 30
        private val IO_TIME_OUT = 30

        override fun baseUrl() = appSettings.baseApiUrl()

        override fun configRetrofit(
            okHttpClient: OkHttpClient,
            builder: Retrofit.Builder
        ): Boolean {
            return false
        }

        override fun configHttp(builder: OkHttpClient.Builder) {
            //常规配置
            builder.connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
            //Api 签名协议
            configApiProtocol(userManager, androidPlatform, builder)
            //调试配置
            configDebugIfNeeded(builder)
        }

        @Suppress("ConstantConditionIf")
        private fun configDebugIfNeeded(builder: OkHttpClient.Builder) {
            //打印日志
            ifOpenLog {
                val httpLoggingInterceptor =
                    HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                        override fun log(message: String) {
                            Timber.tag("===OkHttp===").i(message)
                        }
                    })
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

            builder.authenticator(object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    errorHandler.handleGlobalError(ApiHelper.buildAuthenticationExpiredException())
                    return null
                }
            })

            //HTTPS
            ProtocolUtils.trustAllCertificationChecked(builder)
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

internal fun newErrorBodyParser(): ErrorBodyParser {
    return object : ErrorBodyParser {
        override fun parseErrorBody(errorBody: String): ApiErrorException? {
            val errorResult = JsonUtils.fromClass(errorBody, ErrorResult::class.java)
            return if (errorResult == null) {
                null
            } else {
                ApiErrorException(errorResult.code, errorResult.msg)
            }
        }
    }
}

internal fun newErrorMessage(): ErrorMessage {
    return object : ErrorMessage {
        override fun netErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_net_error)
        }

        override fun serverDataErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_service_data_error)
        }

        override fun serverReturningNullDataErrorMessage(exception: Throwable?): CharSequence {
            return getString(R.string.error_service_no_data_error)
        }

        override fun serverErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_service_error)
        }

        override fun clientRequestErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_request_error)
        }

        override fun apiErrorMessage(exception: ApiErrorException): CharSequence {
            return getString(R.string.error_api_code_mask_tips, exception.code)
        }

        override fun unknownErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_unknow) + "：${exception.message}"
        }
    }
}

internal fun newApiHandler(errorHandler: ErrorHandler): ApiHandler = ApiHandler { result ->
    //登录状态已过期，请重新登录、账号在其他设备登陆
    if (ApiHelper.isLoginExpired(result.code)) {
        errorHandler.handleGlobalError(ApiErrorException(result.code, "登录过期"))
    }
}