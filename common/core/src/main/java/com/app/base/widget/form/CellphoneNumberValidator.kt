package com.app.base.widget.form

import android.view.View
import com.android.base.utils.common.isChinaPhoneNumber
import com.app.base.R

class CellphoneNumberValidator constructor(view: View) : TextValidator(view) {

    public override fun emptyTips(): Int {
        return R.string.please_enter_mobile_phone_number
    }

    public override fun noMatchTips(): Int {
        return R.string.mobile_phone_number_wrong_format
    }

    override fun validateTypeText(content: String): Boolean {
        return isChinaPhoneNumber(content)
    }

}
