package com.app.base.widget.form

import android.view.View
import com.app.base.R

class PasswordValidator constructor(view: View) : TextValidator(view) {

    override fun validateTypeText(content: String): Boolean {
        return content.matchBhPassword()
    }

    public override fun emptyTips(): Int {
        return R.string.please_enter_password
    }

    public override fun noMatchTips(): Int {
        return R.string.password_too_wrong_format_tips
    }

}
