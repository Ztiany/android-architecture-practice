package com.app.base.widget.form

import android.text.TextUtils
import android.view.View

abstract class TextValidator(view: View) : AbsValidator(view) {

    override val isMatch: Boolean
        get() {
            val validateData = validateData
            if (TextUtils.isEmpty(validateData)) {
                handleNoMatch(validateView, emptyTips())
            } else if (!validateTypeText(validateData)) {
                handleNoMatch(validateView, noMatchTips())
            } else {
                handleMatch(validateView)
                return true
            }
            return false
        }

    override val validateData: String
        get() = getStringData(validateView)

    protected abstract fun validateTypeText(content: String): Boolean

    internal abstract fun emptyTips(): Int

    internal abstract fun noMatchTips(): Int

}
