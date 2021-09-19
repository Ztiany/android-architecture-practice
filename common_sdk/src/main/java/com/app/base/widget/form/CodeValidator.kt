package com.app.base.widget.form

import android.view.View
import com.android.base.utils.common.isLengthIn
import com.app.base.R

class CodeValidator constructor(view: View) : TextValidator(view) {

    override fun validateTypeText(content: String): Boolean {
        return isLengthIn(content, 4, 6)
    }

    public override fun emptyTips(): Int {
        return R.string.please_enter_validate_code
    }

    public override fun noMatchTips(): Int {
        return R.string.validate_code_length_error
    }

}
