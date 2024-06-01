package com.app.base.widget.form

import com.android.base.utils.common.isChinaPhoneNumber
import com.android.base.utils.common.isLengthIn

/** 是否符合手机号规范 */
fun CharSequence?.matchCellphone(): Boolean {
    return if (this == null) {
        false
    } else {
        return isChinaPhoneNumber(this.toString())
    }
}

/**是否符合密码规范 */
fun CharSequence?.matchPassword(): Boolean {
    return if (isNullOrBlank()) {
        false
    } else {
        eqPassword(this.toString())
    }
}

/** 密码为 8–16 位，字母、数字、符号组合（必须包含二种或以上），首位为非数字。 */
private fun eqPassword(string: String): Boolean {
    return isLengthIn(string, 8, 16)
            && !string.first().isDigit()
            && (arrayOf(
        string.contains(Regex("[0-9]+")),
        string.contains(Regex("[a-zA-Z]+")),
        string.contains(Regex("[~!@#$%^&*()+<>?/|_.;:,\\-\\[\\]\\\\]+")),
    ).count { it } >= 2)
}