package com.app.base.widget.form

import android.view.View
import com.android.base.utils.common.isChinaPhoneNumber

internal class CellphoneNumberValidator(view: View) : TextValidator(view) {

    public override fun emptyTips(): Int {
        return com.app.base.ui.theme.R.string.please_enter_mobile_phone_number
    }

    public override fun noMatchTips(reason: Int): Int {
        return com.app.base.ui.theme.R.string.mobile_phone_number_wrong_format
    }

    override fun simpleValidateTypeText(content: String): Boolean {
        return isChinaPhoneNumber(content)
    }

}
