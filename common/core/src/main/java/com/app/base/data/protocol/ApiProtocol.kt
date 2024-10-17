package com.app.base.data.protocol

import android.content.Context
import com.android.base.utils.android.views.getString
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.ErrorListener
import com.android.sdk.net.core.provider.ErrorMessageConverter
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.core.provider.PlatformInteractor
import com.android.sdk.net.extension.init
import com.android.sdk.net.extension.setDefaultHostConfig
import com.app.apm.APM
import com.app.apm.reportException
import com.app.base.BuildConfig
import com.app.base.app.Platform
import com.app.base.config.AppSettings
import com.app.base.injection.ApplicationScope
import com.app.base.utils.json.deserializeJson
import com.app.common.api.apiinterceptor.ApiInterceptor
import com.app.common.api.errorhandler.ErrorHandler
import com.app.common.api.usermanager.UserManager
import com.blankj.utilcode.util.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import timber.log.Timber
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

@ApplicationScope
internal class ApiProtocol @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userManager: UserManager,
    private val appSettings: AppSettings,
    private val platform: Platform,
    private val errorHandler: ErrorHandler,
    private val apiInterceptor: Map<String, @JvmSuppressWildcards Provider<ApiInterceptor>>,
) {

    fun initHttpConfig() {
        NetContext.get().init(context) {
            errorMessageConverter(newErrorMessageConverter())
            platformInteractor(newPlatformInteractor(platform))
        }.setDefaultHostConfig {
            httpConfig(newHttpConfig())
            errorBodyParser(newErrorBodyParser(errorHandler))
            errorListener(newErrorListener(errorHandler))
            apiErrorFactory { _, _ -> null }
        }
    }

    private fun newHttpConfig() = object : HttpConfig {

        override fun baseUrl() = appSettings.baseApiUrl()

        override fun configRetrofit(okHttpClient: OkHttpClient, builder: Retrofit.Builder) = false

        override fun configHttp(builder: OkHttpClient.Builder) = with(builder) {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            // HTTPS
            if (BuildConfig.skipHttpCerVerifying) {
                builder.trustAllCertification()
            }
            // API 签名协议
            builder.addInterceptor(ProtocolInterceptor(userManager, platform, apiInterceptor))
            // 打印日志
            APM.installOkHttpLogging(this) { message -> logApiInfo(message) }
            APM.installStethoHttp(builder)
            // API 调试配置
            if (!APM.debugMode) {
                builder.proxy(Proxy.NO_PROXY)
            }
        }
    }


    private fun newPlatformInteractor(platform: Platform): PlatformInteractor {
        return object : PlatformInteractor {
            override fun isConnected(): Boolean {
                return platform.isConnected()
            }
        }
    }

    private fun newErrorBodyParser(errorHandler: ErrorHandler): ErrorBodyParser {
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

    private fun newErrorMessageConverter(): ErrorMessageConverter {
        return object : ErrorMessageConverter {
            override fun netErrorMessage(throwable: Throwable): CharSequence {
                if (NetworkUtils.isConnected()) {
                    return getString(com.app.base.ui.theme.R.string.error_service_error)
                }
                return getString(com.app.base.ui.theme.R.string.error_net_error)
            }

            override fun serverDataParseErrorMessage(throwable: Throwable): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_service_data_error)
            }

            override fun nullEntityErrorMessage(throwable: Throwable): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_service_no_data_error)
            }

            override fun serverInternalErrorMessage(throwable: Throwable): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_service_error)
            }

            override fun clientRequestErrorMessage(throwable: Throwable): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_request_error)
            }

            override fun apiErrorMessage(exception: ApiErrorException): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_api_code_mask_tips, ResponseCode.name(exception.code))
            }

            override fun unknownErrorMessage(throwable: Throwable): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_unknown) + "：${throwable.message}"
            }
        }
    }

    private fun newErrorListener(errorHandler: ErrorHandler) = object : ErrorListener {
        override fun onApiErrorException(exception: ApiErrorException, hostFlag: String) {
            APM.reportException(exception)
            Timber.w("ApiHandler exception: $exception, hostFlag = $hostFlag")
            errorHandler.handleGlobalError(exception)
        }

        override fun onServerDataEmptyError(exception: ServerErrorException, hostFlag: String) {
            APM.reportException(exception)
            Timber.w("onServerDataEmptyError exception: $exception, hostFlag = $hostFlag")
            errorHandler.handleGlobalError(exception)
        }

        override fun onServerDataParseError(exception: ServerErrorException, hostFlag: String) {
            APM.reportException(exception)
            Timber.w("onServerDataParseError exception: $exception, hostFlag = $hostFlag}")
            errorHandler.handleGlobalError(exception)
        }
    }

}