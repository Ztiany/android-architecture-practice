/**用于定义一些业务上的常量,以及通用的标识判断*/
@file:JvmName("Business")

package com.app.base.data.api

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