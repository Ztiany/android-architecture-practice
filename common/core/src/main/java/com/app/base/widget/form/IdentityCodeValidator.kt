package com.app.base.widget.form

import android.view.View
import com.app.base.R

class IdentityCodeValidator constructor(view: View) : TextValidator(view) {

    override fun validateTypeText(content: String): Boolean {
        return content.length == 15 || content.length == 18
    }

    public override fun emptyTips(): Int {
        return R.string.please_enter_paperwork_code
    }

    public override fun noMatchTips(): Int {
        return R.string.please_enter_legitimate_paperwork_code
    }

}
