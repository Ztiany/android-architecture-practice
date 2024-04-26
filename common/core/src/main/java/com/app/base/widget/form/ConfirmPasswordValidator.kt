package com.app.base.widget.form

import android.text.TextUtils
import android.view.View
import com.app.base.ui.R as UI_R

class ConfirmPasswordValidator(view: View, private val confirmPassword: String) : TextValidator(view) {

    override fun validateTypeText(content: String): Boolean {
        return !TextUtils.isEmpty(confirmPassword) && confirmPassword == content
    }

    public override fun emptyTips(): Int {
        return UI_R.string.please_enter_confirm_password
    }

    public override fun noMatchTips(): Int {
        return UI_R.string.enter_the_password_twice_inconsistent
    }

}