package me.ztiany.wan.sample.common.net

import com.android.sdk.net.core.result.Result
import com.google.gson.annotations.SerializedName

data class SampleApiResult<T>(
    @SerializedName("data") override val data: T,
    @SerializedName("errorCode") override val code: Int,
    @SerializedName("errorMsg") override val message: String
) : Result<T> {

    init {
        println("ApiResult Constructor is called.")
    }

    override val isSuccess: Boolean
        get() = code == 0

}