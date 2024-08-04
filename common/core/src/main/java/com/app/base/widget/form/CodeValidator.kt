package com.app.base.widget.form

import android.view.View
import com.android.base.utils.common.isLengthIn

internal class CodeValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        return isLengthIn(content, 6, 6)
    }

    public override fun emptyTips(): Int {
        return com.app.base.ui.theme.R.string.please_enter_validate_code
    }

    public override fun noMatchTips(reason: Int): Int {
        return com.app.base.ui.theme.R.string.validate_code_length_error
    }

}
