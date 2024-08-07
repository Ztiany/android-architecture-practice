package com.app.base.utils.form

import android.view.View
import com.android.base.utils.common.isLengthIn

internal class PasswordValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        // 已有的密码只对长度进行校验
        return isLengthIn(content, 8, 16)
    }

    public override fun emptyTips(): Int {
        return com.app.base.ui.theme.R.string.please_enter_password
    }

    public override fun noMatchTips(reason: Int): Int {
        return com.app.base.ui.theme.R.string.password_format_tips
    }

}
