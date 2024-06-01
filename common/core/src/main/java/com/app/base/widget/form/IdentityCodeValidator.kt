package com.app.base.widget.form

import android.view.View
import com.app.base.ui.R as UI_R

internal class IdentityCodeValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        return content.length == 15 || content.length == 18
    }

    public override fun emptyTips(): Int {
        return UI_R.string.please_enter_paperwork_code
    }

    public override fun noMatchTips(reason: Int): Int {
        return UI_R.string.please_enter_legitimate_paperwork_code
    }

}
