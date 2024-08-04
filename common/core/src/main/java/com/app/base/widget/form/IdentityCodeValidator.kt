package com.app.base.widget.form

import android.view.View

internal class IdentityCodeValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        return content.length == 15 || content.length == 18
    }

    public override fun emptyTips(): Int {
        return com.app.base.ui.theme.R.string.please_enter_paperwork_code
    }

    public override fun noMatchTips(reason: Int): Int {
        return com.app.base.ui.theme.R.string.please_enter_legitimate_paperwork_code
    }

}
