package com.app.base.widget.form

import android.view.View
import com.app.base.R

class ImageCodeValidator constructor(view: View) : TextValidator(view) {

    override fun validateTypeText(content: String): Boolean {
        return content.isNotEmpty()
    }

    public override fun emptyTips(): Int {
        return R.string.please_enter_image_validate_code
    }

    public override fun noMatchTips(): Int {
        return R.string.please_enter_image_validate_code
    }

}
