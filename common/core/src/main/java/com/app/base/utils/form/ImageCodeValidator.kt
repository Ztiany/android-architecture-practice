package com.app.base.utils.form

import android.view.View

internal class ImageCodeValidator(view: View) : TextValidator(view) {

    override fun simpleValidateTypeText(content: String): Boolean {
        return content.isNotEmpty()
    }

    public override fun emptyTips(): Int {
        return com.app.base.ui.theme.R.string.please_enter_image_validate_code
    }

    public override fun noMatchTips(reason: Int): Int {
        return com.app.base.ui.theme.R.string.please_enter_image_validate_code
    }

}
