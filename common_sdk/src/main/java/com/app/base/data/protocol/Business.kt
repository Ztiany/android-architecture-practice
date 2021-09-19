/**用于定义一些业务上的常量，以及通用的标识判断*/
@file:JvmName("Business")

package com.app.base.data.protocol

import androidx.annotation.IntDef

/**未初始化ID*/
const val UNAVAILABLE_FLAG = 0

///////////////////////////////////////////////////////////////////////////
// 后台返回统一的 true 和 false 状态
///////////////////////////////////////////////////////////////////////////
const val FLAG_POSITIVE = 1
const val FLAG_NEGATIVE = 0

fun isFlagPositive(flag: Int): Boolean {
    return flag == FLAG_POSITIVE
}

///////////////////////////////////////////////////////////////////////////
// 性别
///////////////////////////////////////////////////////////////////////////
const val SEX_MALE = 1
const val SEX_FEMALE = 2

@IntDef(value = [SEX_MALE, SEX_FEMALE])
annotation class SexType