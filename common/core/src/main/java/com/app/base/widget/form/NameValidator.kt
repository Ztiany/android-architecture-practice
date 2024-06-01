package com.app.base.widget.form

import android.view.View
import com.android.base.utils.common.isChineseName
import com.app.base.ui.R as UI_R

internal class NameValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        return isChineseName(content)
    }

    public override fun emptyTips(): Int {
        return UI_R.string.please_enter_name
    }

    public override fun noMatchTips(reason: Int): Int {
        return UI_R.string.please_enter_legitimate_name
    }

}
