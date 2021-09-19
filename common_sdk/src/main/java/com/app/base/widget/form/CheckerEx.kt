package com.app.base.widget.form

import com.android.base.utils.common.*
import java.util.regex.Pattern

/**是否符合手机号规范*/
fun CharSequence?.matchCellphone(): Boolean {
    return if (this == null) {
        false
    } else {
        return isChinaPhoneNumber(this.toString())
    }
}

/**是否符合密码规范*/
fun CharSequence?.matchBhPassword(): Boolean {
    return if (this == null) {
        false
    } else {
        eqBhPassword(this.toString())
    }
}

/**
 * 密码由8–15位的字母大小写、数字、特殊字符其中三种或以上组成，不允许空格，特殊字符包括
 */
private fun eqBhPassword(string: String): Boolean {
    return isLengthIn(string, 8, 15) && !string.contains(" ") && listOf(
            containsLowercaseLetter(string),
            containsDigital(string),
            containsUppercaseLetter(string),
            /*特殊字符：.~!@#$%^&*()/|,<>?"';:_+-=[]{}*/
            Pattern.matches("^.*[\\.~!@#$%^&*()/|,<>?\"';:_+\\-=\\[\\]{}]+.*$", string)
    ).count { it } >= 3
}