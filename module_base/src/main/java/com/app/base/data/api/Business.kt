/**用于定义一些业务上的常量,以及通用的标识判断*/
@file:JvmName("Business")

package com.app.base.data.api

import androidx.annotation.IntDef

///////////////////////////////////////////////////////////////////////////
// 后台返回统一的true和false状态
///////////////////////////////////////////////////////////////////////////
const val FLAG_POSITIVE = 1
const val FLAG_NEGATIVE = 0

fun isFlagPositive(flag: Int): Boolean {
    return flag == FLAG_POSITIVE
}

fun isDefault(flagCode: Int): Boolean {
    return FLAG_POSITIVE == flagCode
}

fun isSelected(flag: Int): Boolean {
    return FLAG_POSITIVE == flag
}


///////////////////////////////////////////////////////////////////////////
// 短信验证码类型
///////////////////////////////////////////////////////////////////////////
const val SMS_TYPE_REGISTER = 1
const val SMS_TYPE_FORGET_PASSWORD = 2
const val SMS_TYPE_LOGIN_VALIDATE = 3
const val SMS_TYPE_MODIFY_PASSWORD = 4
const val SMS_TYPE_MODIFY_PHONE = 5
const val SMS_TYPE_BINDING_NEW_PHONE = 6
const val SMS_TYPE_DOWNLOADING_REPORT = 7

@IntDef(value = [
    SMS_TYPE_REGISTER,
    SMS_TYPE_FORGET_PASSWORD,
    SMS_TYPE_LOGIN_VALIDATE,
    SMS_TYPE_MODIFY_PASSWORD,
    SMS_TYPE_DOWNLOADING_REPORT,
    SMS_TYPE_BINDING_NEW_PHONE,
    SMS_TYPE_MODIFY_PHONE])
annotation class SmsType