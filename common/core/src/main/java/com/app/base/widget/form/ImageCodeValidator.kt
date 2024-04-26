package com.app.base.widget.form

import android.view.View
import com.app.base.ui.R as UI_R

class ImageCodeValidator(view: View) : TextValidator(view) {

    override fun validateTypeText(content: String): Boolean {
        return content.isNotEmpty()
    }

    public override fun emptyTips(): Int {
        return UI_R.string.please_enter_image_validate_code
    }

    public override fun noMatchTips(): Int {
        return UI_R.string.please_enter_image_validate_code
    }

}
