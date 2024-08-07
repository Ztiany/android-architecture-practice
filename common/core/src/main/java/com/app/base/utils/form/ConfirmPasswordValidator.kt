package com.app.base.utils.form

import android.text.TextUtils
import android.view.View

internal class ConfirmPasswordValidator(view: View, private val confirmPassword: String) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        return !TextUtils.isEmpty(confirmPassword) && confirmPassword == content
    }

    public override fun emptyTips(): Int {
        return com.app.base.ui.theme.R.string.please_enter_confirm_password
    }

    public override fun noMatchTips(reason: Int): Int {
        return com.app.base.ui.theme.R.string.enter_the_password_twice_inconsistent
    }

}