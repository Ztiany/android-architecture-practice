package com.app.base.widget.form

import android.view.View
import com.app.base.ui.R as UI_R

/** 验证旧密码。*/
internal class OldPasswordValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        // 不对旧密码做格式校验
        return true
    }

    public override fun emptyTips(): Int {
        return UI_R.string.please_enter_old_password
    }

    public override fun noMatchTips(reason: Int): Int {
        return UI_R.string.please_enter_old_password
    }

}
