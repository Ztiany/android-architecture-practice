package com.app.base.data.protocol

import android.content.Context
import com.android.base.utils.android.views.getString
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.provider.ApiHandler
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.ErrorMessage
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.core.provider.PlatformInteractor
import com.android.sdk.net.extension.init
import com.android.sdk.net.extension.setDefaultHostConfig
import com.app.apm.APM
import com.app.base.BuildConfig
import com.app.base.app.Platform
import com.app.base.config.AppSettings
import com.app.base.injection.ApplicationScope
import com.app.base.ui.theme.R
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
            errorMessage(newErrorMessage())
            platformInteractor(newPlatformInteractor(platform))
        }.setDefaultHostConfig {
            httpConfig(newHttpConfig())
            aipHandler(newApiHandler())
            errorBodyHandler(newErrorBodyParser())
            exceptionFactory { _, _ -> null }
        }
    }

    private fun newErrorBodyParser() = object : ErrorBodyParser {
        override fun parseErrorBody(errorBody: String, hostFlag: String): ApiErrorException? {
            return null
        }
    }

    private fun newApiHandler(): ApiHandler = ApiHandler { result, hostFlag ->
        Timber.d("ApiHandler result: $result")
        //登录状态已过期，请重新登录、账号在其他设备登陆
        errorHandler.handleGlobalError(ApiErrorException(result.code, result.message, hostFlag))
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
            builder.addInterceptor(ProtocolInterceptor(
                userManager,
                platform,
                apiInterceptor
            ))
            // 打印日志
            APM.installOkHttpLogging(this) { message -> logApiInfo(message) }
            APM.installStethoHttp(builder)
            // API 调试配置
            if (!APM.debugMode) {
                builder.proxy(Proxy.NO_PROXY)
            }
        }
    }

    private fun newPlatformInteractor(platform: Platform) = object : PlatformInteractor {
        override fun isConnected(): Boolean {
            return platform.isConnected()
        }
    }

    private fun newErrorMessage() = object : ErrorMessage {

        override fun netErrorMessage(exception: Throwable): CharSequence {
            if (NetworkUtils.isConnected()) {
                return getString(R.string.error_service_error)
            }
            return getString(R.string.error_net_error)
        }

        override fun serverDataErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_service_data_error)
        }

        override fun serverReturningNullEntityErrorMessage(exception: Throwable?): CharSequence {
            return getString(R.string.error_service_no_data_error)
        }

        override fun serverInternalErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_service_error)
        }

        override fun clientRequestErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_request_error)
        }

        override fun apiErrorMessage(exception: ApiErrorException): CharSequence {
            return getString(R.string.error_api_code_mask_tips, exception.code)
        }

        override fun unknownErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_unknown) + "：${exception.message}"
        }
    }

}