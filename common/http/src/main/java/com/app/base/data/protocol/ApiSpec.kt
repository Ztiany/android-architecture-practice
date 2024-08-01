package com.app.base.data.protocol

import androidx.annotation.IntDef
import androidx.annotation.StringDef
import com.app.base.data.protocol.SexType.Companion.API_SEX_FEMALE
import com.app.base.data.protocol.SexType.Companion.API_SEX_MALE
import com.app.base.data.protocol.SmsType.Companion.API_SMS_CODE_CLOSE
import com.app.base.data.protocol.SmsType.Companion.API_SMS_CODE_LOGIN
import com.app.base.data.protocol.SmsType.Companion.API_SMS_CODE_RETRIEVE

/* 这里定义了一些 API 相关的常量，比如 API 的状态码，性别，验证码类型等。 */

///////////////////////////////////////////////////////////////////////////
// 后台返回统一的 true 和 false 状态
///////////////////////////////////////////////////////////////////////////
annotation class ApiFlag {

    companion object {

        /** 未初始化的 ID */
        const val API_UNAVAILABLE_FLAG = -1

        const val API_FLAG_POSITIVE = 1
        const val API_FLAG_NEGATIVE = 0

        fun isPositiveApiFlag(code: Int): Boolean {
            return code == API_FLAG_POSITIVE
        }

    }

}

///////////////////////////////////////////////////////////////////////////
// 验证码类型
///////////////////////////////////////////////////////////////////////////

@StringDef(API_SMS_CODE_LOGIN, API_SMS_CODE_RETRIEVE, API_SMS_CODE_CLOSE)
annotation class SmsType {

    companion object {
        /** 登录 */
        const val API_SMS_CODE_LOGIN = "001"

        /** 找回密码 */
        const val API_SMS_CODE_RETRIEVE = "003"

        /** 账号注销 */
        const val API_SMS_CODE_CLOSE = "004"
    }

}

///////////////////////////////////////////////////////////////////////////
// 性别
///////////////////////////////////////////////////////////////////////////

@IntDef(API_SEX_MALE, API_SEX_FEMALE)
annotation class SexType {

    companion object {
        const val API_SEX_MALE = 1
        const val API_SEX_FEMALE = 2
    }

}