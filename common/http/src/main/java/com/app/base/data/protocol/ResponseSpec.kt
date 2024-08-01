package com.app.base.data.protocol

import com.android.sdk.net.core.exception.ApiErrorException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

enum class ResponseCode(val code: Int) {

    /** 成功 */
    OK(1),

    /** 秘钥交换失败  or 登录失效 */
    KeyLost(3008);

    fun isMe(code: Int): Boolean {
        return this.code == code
    }

    companion object {
        fun name(code: Int): String {
            return entries.find {
                it.code == code
            }?.let {
                return it.name
            } ?: "[Undefined-${code}]"
        }
    }
}

internal fun ApiResult<*>.isApiResponseSuccess(): Boolean {
    return ResponseCode.OK.isMe(code)
}

@OptIn(ExperimentalContracts::class)
fun Throwable.isSpecifiedApiError(errorCode: ResponseCode): Boolean {
    contract {
        returns(true) implies (this@isSpecifiedApiError is ApiErrorException)
    }
    return this is ApiErrorException && errorCode.isMe(code)
}

@OptIn(ExperimentalContracts::class)
fun Throwable.isSpecifiedApiError(errorCode: String): Boolean {
    contract {
        returns(true) implies (this@isSpecifiedApiError is ApiErrorException)
    }
    return this is ApiErrorException && errorCode.hashCode() == code
}

@OptIn(ExperimentalContracts::class)
fun Throwable.isApiAuthenticationExpired(): Boolean {
    contract {
        returns(true) implies (this@isApiAuthenticationExpired is ApiErrorException)
    }
    return isSpecifiedApiError(ResponseCode.KeyLost)
}

@OptIn(ExperimentalContracts::class)
fun Throwable.isGlobalApiError(): Boolean {
    contract {
        returns(true) implies (this@isGlobalApiError is ApiErrorException)
    }
    return isApiAuthenticationExpired()
}