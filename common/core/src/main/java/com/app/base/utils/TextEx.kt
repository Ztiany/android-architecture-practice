package com.app.base.utils

/**超过 [maxSize] 显示 [maxSize] 个字符 + [replace]*/
fun CharSequence?.foldText(maxSize: Int, replace: String = "..."): CharSequence {
    return if (this == null || this.length <= maxSize) {
        ""
    } else {
        "${this.subSequence(0, maxSize)}$replace"
    }
}

/**超过 [maxSize] 显示 [maxSize] 个字符 + [replace]*/
fun String?.foldText(maxSize: Int, replace: String = "..."): String {
    return if (this.isNullOrEmpty() || this.length <= maxSize) {
        this ?: ""
    } else {
        "${this.subSequence(0, maxSize)}$replace"
    }
}

/** 隐藏 11 位手机号的 4-7 位*/
fun hidePhoneNumber(phoneNumber: String?): String {
    if (phoneNumber == null) {
        return ""
    }
    if (phoneNumber.length != 11) {
        return phoneNumber
    }
    return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7)
}

/** 隐藏 18 位身份证：前3位+“12位*”+后3位，一共18位*/
fun hideIDCardNumber(iDCardNumber: String?): String {
    iDCardNumber ?: return ""
    val length = iDCardNumber.length
    return if (length <= 8) {
        iDCardNumber
    } else {
        iDCardNumber.substring(0, 4) + ("*".repeat(length - 8)) + iDCardNumber.substring(length - 4)
    }
}

fun combineAddress(province: String, city: String, area: String): String {
    return listOf(province, city, area).filter { it.isNotEmpty() }.joinToString("、")
}