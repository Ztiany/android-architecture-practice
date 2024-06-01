package com.app.base.widget.form

import android.view.View
import com.android.base.utils.common.isLengthIn
import com.app.base.ui.R  as UI_R

internal class CodeValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        return isLengthIn(content, 6, 6)
    }

    public override fun emptyTips(): Int {
        return UI_R.string.please_enter_validate_code
    }

    public override fun noMatchTips(reason: Int): Int {
        return UI_R.string.validate_code_length_error
    }

}
