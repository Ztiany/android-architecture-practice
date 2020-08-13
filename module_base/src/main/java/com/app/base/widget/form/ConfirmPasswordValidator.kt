package com.app.base.widget.form

import android.text.TextUtils
import android.view.View
import com.app.base.R

class ConfirmPasswordValidator constructor(view: View, private val confirmPassword: String) : TextValidator(view) {

    override fun validateTypeText(content: String): Boolean {
        return !TextUtils.isEmpty(confirmPassword) && confirmPassword == content
    }

    public override fun emptyTips(): Int {
        return R.string.please_enter_confirm_password
    }

    public override fun noMatchTips(): Int {
        return R.string.enter_the_password_twice_inconsistent
    }

}