package com.app.base.widget.form

import android.view.View
import com.android.base.utils.common.isChinaPhoneNumber
import com.app.base.ui.R as UI_R

class CellphoneNumberValidator(view: View) : TextValidator(view) {

    public override fun emptyTips(): Int {
        return UI_R.string.please_enter_mobile_phone_number
    }

    public override fun noMatchTips(): Int {
        return UI_R.string.mobile_phone_number_wrong_format
    }

    override fun validateTypeText(content: String): Boolean {
        return isChinaPhoneNumber(content)
    }

}
