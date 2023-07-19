package com.app.base.widget.form

import com.android.base.utils.common.isChinaPhoneNumber
import com.android.base.utils.common.isLengthIn
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
fun CharSequence?.matchPassword(): Boolean {
    return if (this == null) {
        false
    } else {
        eqPassword(this.toString())
    }
}

/**
 * 密码由 8–16 非特殊字符组成
 */
private fun eqPassword(string: String): Boolean {
    return isLengthIn(string, 8, 16)
            && !string.contains(" ")
            && !Pattern.matches("^.*[\\\\.~!@#$%^&*()/|,<>?\"';:_+\\-=\\[\\]{}]+.*$", string)
}