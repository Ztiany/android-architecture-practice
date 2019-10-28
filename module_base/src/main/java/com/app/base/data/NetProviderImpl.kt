package com.app.base.data

import com.android.base.utils.android.views.getString
import com.android.sdk.net.exception.ApiErrorException
import com.android.sdk.net.provider.*
import com.app.base.BuildConfig
import com.app.base.R
import com.app.base.data.api.ApiHelper
import com.app.base.debug.DebugTools
import io.reactivex.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.reactivestreams.Publisher
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


internal fun newHttpConfig(): HttpConfig {

    return object : HttpConfig {

        private val CONNECTION_TIME_OUT = 10
        private val IO_TIME_OUT = 20

        override fun baseUrl() = DataContext.baseUrl()
        override fun configRetrofit(okHttpClient: OkHttpClient, builder: Retrofit.Builder)= false

        override fun configHttp(builder: OkHttpClient.Builder) {
            //常规配置
            builder.connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                    .readTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
                    .hostnameVerifier { hostname, session ->
                        Timber.d("hostnameVerifier called with: hostname 、session = [" + hostname + "、" + session.protocol + "]")
                        true
                    }

            //调试配置
            if (BuildConfig.openDebug) {
                val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("===OkHttp===").i(message) }
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(httpLoggingInterceptor)
                DebugTools.installStethoHttp(builder)
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
            return getString(R.string.error_unknow)
        }
    }
}

internal fun newErrorDataAdapter(): ErrorDataAdapter = object : ErrorDataAdapter {
    override fun createErrorDataStub(type: Type, annotations: Array<Annotation>, retrofit: Retrofit, value: ResponseBody): Any {
        return ApiHelper.newErrorDataStub()
    }

    override fun isErrorDataStub(`object`: Any): Boolean {
        return ApiHelper.isDataError(`object`)
    }
}

internal fun newPostTransformer(): PostTransformer<*> = object : PostTransformer<Any> {
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

internal fun newApiHandler(): ApiHandler = ApiHandler { result ->
    //登录状态已过期，请重新登录、账号在其他设备登陆
    if (ApiHelper.isLoginExpired(result.code)) {
        DataContext.getInstance().publishLoginExpired(result.code)
    }
}