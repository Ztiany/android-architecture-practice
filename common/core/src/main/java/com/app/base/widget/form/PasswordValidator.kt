package com.app.base.widget.form

import android.view.View
import com.android.base.utils.common.isLengthIn
import com.app.base.ui.R as UI_R

internal class PasswordValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        // 已有的密码只对长度进行校验
        return isLengthIn(content, 8, 16)
    }

    public override fun emptyTips(): Int {
        return UI_R.string.please_enter_password
    }

    public override fun noMatchTips(reason: Int): Int {
        return UI_R.string.password_format_tips
    }

}
