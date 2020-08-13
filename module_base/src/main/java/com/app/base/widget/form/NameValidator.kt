package com.app.base.widget.form

import android.view.View
import com.android.base.utils.common.isChineseName
import com.app.base.R

class NameValidator constructor(view: View) : TextValidator(view) {

    override fun validateTypeText(content: String): Boolean {
        return isChineseName(content)
    }

    public override fun emptyTips(): Int {
        return R.string.please_enter_name
    }

    public override fun noMatchTips(): Int {
        return R.string.please_enter_legitimate_name
    }

}
