package com.app.base.data.protocol

import com.android.sdk.net.core.result.Result
import com.google.gson.annotations.SerializedName

data class HttpResult<T>(
    @SerializedName("data") override val data: T,
    @SerializedName("errorCode") override val code: Int,
    @SerializedName("errorMsg") override val message: String
) : Result<T> {

    init {
        println("HttpResult Constructor Called.")
    }

    override val isSuccess: Boolean
        get() = ApiHelper.isSuccess(this)
}