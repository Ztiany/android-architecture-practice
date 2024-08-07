package com.app.base.utils.form

import android.view.View
import java.util.regex.Pattern

/**前后端统一邮箱校验*/
private const val EMAIL_REG = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"

internal class EmailValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        return isEmail(content)
    }

    public override fun emptyTips(): Int {
        return com.app.base.ui.theme.R.string.please_enter_email
    }

    public override fun noMatchTips(reason: Int): Int {
        return com.app.base.ui.theme.R.string.email_wrong_format
    }

    private fun isEmail(email: String?): Boolean {
        return !email.isNullOrBlank() && Pattern.matches(EMAIL_REG, email)
    }

}