package com.app.base.data.protocol

import com.android.sdk.net.core.https.HttpsUtils
import com.app.apm.APM
import com.app.apm.doIfLogEnabled
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.net.URLEncoder

fun String.toJsonRequestBody(): RequestBody {
    return this.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}

/**
 * 生成表单请求体。如何上传 URI？参考：
 *
 *  - [okhttp/issues/3585](https://github.com/square/okhttp/issues/3585)
 *  - [net/issues/189](https://github.com/liangjingkanji/Net/issues/189)
 */
fun buildMultiPartRequestBody(fieldParts: Map<String, String>, fileParts: Map<String, File>): Map<String, RequestBody> {
    val params: MutableMap<String, RequestBody> = LinkedHashMap()

    for ((key, value) in fieldParts) {
        params[key] = RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

    for ((key, value) in fileParts) {
        params[URLEncoder.encode(key) + "\"; filename=\"" + URLEncoder.encode(value.name)] = RequestBody.create(null, value)
    }

    return params
}

/**
 * Make OkHttpClient trust all https protocol (It is not safe).
 * check [javax-net-ssl-sslhandshakeexception-java-lang-illegalargumentexception-invalid](https://stackoverflow.com/questions/51563859/javax-net-ssl-sslhandshakeexception-java-lang-illegalargumentexception-invalid)
 * for more details.
 */
internal fun OkHttpClient.Builder.trustAllCertification() {
    val sslSocketFactory = HttpsUtils.getSslSocketFactory(null, null, null)
    sslSocketFactory(sslSocketFactory.sSLSocketFactory, sslSocketFactory.trustManager).hostnameVerifier { _, _ -> true }
}

private const val API_TAG = "===OkHttp==="

internal fun logApiInfo(content: String) {
    APM.doIfLogEnabled {
        Timber.tag(API_TAG).i(content)
    }
}

internal fun logApiError(error: Throwable, content: String) {
    APM.doIfLogEnabled {
        Timber.tag(API_TAG).e(error, content)
    }
}

internal fun logApiWarning(content: String) {
    APM.doIfLogEnabled {
        Timber.tag(API_TAG).w(content)
    }
}