package com.app.base.widget.form

import android.view.View

/** 验证旧密码。*/
internal class NewPasswordValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        // 不对旧密码做格式校验
        return content.matchPassword()
    }

    public override fun emptyTips(): Int {
        return com.app.base.ui.theme.R.string.please_enter_new_password
    }

    public override fun noMatchTips(reason: Int): Int {
        return com.app.base.ui.theme.R.string.new_password_format_tips
    }

}
