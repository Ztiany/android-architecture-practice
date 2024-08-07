package com.app.base.utils.form

import android.view.View

/** 验证旧密码。*/
internal class OldPasswordValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        // 不对旧密码做格式校验
        return true
    }

    public override fun emptyTips(): Int {
        return com.app.base.ui.theme.R.string.please_enter_old_password
    }

    public override fun noMatchTips(reason: Int): Int {
        return com.app.base.ui.theme.R.string.please_enter_old_password
    }

}
