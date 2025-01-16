package com.app.base.data.protocol

import android.content.Context
import com.android.base.utils.android.views.getString
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.config.ErrorListener
import com.android.sdk.net.core.config.ErrorMessageConverter
import com.android.sdk.net.core.config.HttpConfig
import com.android.sdk.net.core.config.PlatformInteractor
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.extension.configDefaultHost
import com.android.sdk.net.extension.init
import com.app.apm.APM
import com.app.base.BuildConfig
import com.app.base.app.Platform
import com.app.base.config.AppSettings
import com.app.base.injection.ApplicationScope
import com.app.common.api.apiinterceptor.ApiInterceptor
import com.app.common.api.errorhandler.ErrorHandler
import com.app.common.api.usermanager.UserManager
import com.blankj.utilcode.util.NetworkUtils
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
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
        }.configDefaultHost {
            httpConfig(newHttpConfig())
            errorListener(newErrorListener(errorHandler))
        }
    }

    private fun newHttpConfig() = object : HttpConfig {

        override fun configRetrofit( builder: Retrofit.Builder) {
            builder.baseUrl(appSettings.baseApiUrl())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
        }

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


    private fun newErrorMessageConverter(): ErrorMessageConverter {
        return object : ErrorMessageConverter {
            override fun convertWhenNetError(throwable: IOException): CharSequence {
                if (NetworkUtils.isConnected()) {
                    return getString(com.app.base.ui.theme.R.string.error_service_error)
                }
                return getString(com.app.base.ui.theme.R.string.error_net_error)
            }

            override fun convertWhenParsingDataFailed(throwable: ServerErrorException): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_service_data_error)
            }

            override fun convertWhenNoDataReturned(throwable: ServerErrorException): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_service_no_data_error)
            }

            override fun convertWhenServerInternalError(throwable: HttpException): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_service_error)
            }

            override fun convertWhenClientRequestFailed(throwable: HttpException): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_request_error)
            }

            override fun convertWhenApiException(exception: ApiErrorException): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_api_code_mask_tips, ResponseCode.name(exception.code))
            }

            override fun convertWhenUnknownError(throwable: Throwable): CharSequence {
                return getString(com.app.base.ui.theme.R.string.error_unknown) + "：${throwable.message}"
            }
        }
    }

    private fun newErrorListener(errorHandler: ErrorHandler) = object : ErrorListener {
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

}