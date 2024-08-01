package com.app.base.data.protocol

import com.android.sdk.net.core.result.Result
import com.google.gson.annotations.SerializedName

/** APP HTTP 请求响应体格式实现 */
data class ApiResult<T>(
    @SerializedName("data") override val data: T,
    @SerializedName("errorCode") override val code: Int,
    @SerializedName("errorMsg") override val message: String
) : Result<T> {

    override val isSuccess: Boolean
        get() = isApiResponseSuccess()

    override fun toString(): String {
        return "ApiResult(data=$data, code=${ResponseCode.name(code)}, message='$message')"
    }

}