package com.app.base.widget.form

import android.view.View
import com.android.base.utils.common.isEmpty
import com.app.base.R
import java.util.regex.Pattern

/**前后端统一邮箱校验*/
private const val EMAIL_REG = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"

class EmailValidator constructor(view: View) : TextValidator(view) {

    override fun validateTypeText(content: String): Boolean {
        return isEmail(content)
    }

    public override fun emptyTips(): Int {
        return R.string.please_enter_email
    }

    public override fun noMatchTips(): Int {
        return R.string.email_wrong_format
    }

    private fun isEmail(email: String?): Boolean {
        return !isEmpty(email) && Pattern.matches(EMAIL_REG, email)
    }

}