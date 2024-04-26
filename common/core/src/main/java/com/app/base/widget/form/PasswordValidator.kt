package com.app.base.widget.form

import android.view.View
import com.app.base.ui.R as UI_R

class PasswordValidator(view: View) : TextValidator(view) {

    override fun validateTypeText(content: String): Boolean {
        return content.matchPassword()
    }

    public override fun emptyTips(): Int {
        return UI_R.string.please_enter_password
    }

    public override fun noMatchTips(): Int {
        return UI_R.string.password_too_wrong_format_tips
    }

}
