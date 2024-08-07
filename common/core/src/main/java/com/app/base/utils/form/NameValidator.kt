package com.app.base.utils.form

import android.view.View
import com.android.base.utils.common.isChineseName

internal class NameValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        return isChineseName(content)
    }

    public override fun emptyTips(): Int {
        return com.app.base.ui.theme.R.string.please_enter_name
    }

    public override fun noMatchTips(reason: Int): Int {
        return com.app.base.ui.theme.R.string.please_enter_legitimate_name
    }

}
