package com.app.base.app

import com.android.base.utils.android.views.getString
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.provider.ApiHandler
import com.android.sdk.net.core.provider.ErrorDataAdapter
import com.android.sdk.net.core.provider.ErrorMessage
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.rxjava.RxResultPostTransformer
import com.app.base.R
import com.app.base.config.AppSettings
import com.app.base.data.protocol.ApiHelper
import com.app.base.data.protocol.ProtocolUtils
import com.app.base.data.protocol.configApiProtocol
import com.app.base.debug.DebugTools
import com.app.base.debug.ifOpenLog
import com.app.base.debug.isOpenDebug
import com.app.base.services.usermanager.UserManager
import io.reactivex.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.reactivestreams.Publisher
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.reflect.Type
import java.net.Proxy
import java.util.concurrent.TimeUnit

internal fun newHttpConfig(
    userManager: UserManager,
    appSettings: AppSettings,
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
            configApiProtocol(userManager, builder)
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
            ProtocolUtils.trustAllCertificationChecked(userManager, builder)
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

        override fun serverErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_service_error)
        }

        override fun clientRequestErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_request_error)
        }

        override fun apiErrorMessage(exception: ApiErrorException): CharSequence {
            return getString(R.string.error_api_code_mask_tips, exception.code)
        }

        override fun unknowErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_unknow) + "：${exception.message}"
        }
    }
}

internal fun newErrorDataAdapter(): ErrorDataAdapter = object : ErrorDataAdapter {
    override fun createErrorDataStub(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
        value: ResponseBody
    ): Any {
        return ApiHelper.newErrorDataStub()
    }

    override fun isErrorDataStub(value: Any): Boolean {
        return ApiHelper.isDataError(value)
    }
}

internal fun newPostTransformer(): RxResultPostTransformer<*> =
    object : RxResultPostTransformer<Any> {
        override fun apply(upstream: Single<Any>): SingleSource<Any> {
            return upstream
        }

        override fun apply(upstream: Flowable<Any>): Publisher<Any> {
            return upstream
        }

        override fun apply(upstream: Observable<Any>): ObservableSource<Any> {
            return upstream
        }
    }

internal fun newApiHandler(errorHandler: ErrorHandler): ApiHandler = ApiHandler { result ->
    //登录状态已过期，请重新登录、账号在其他设备登陆
    if (ApiHelper.isLoginExpired(result.code)) {
        errorHandler.handleError(ApiErrorException(result.code, "登录过期"))
    }
}