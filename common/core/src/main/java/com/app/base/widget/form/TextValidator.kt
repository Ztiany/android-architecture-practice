package com.app.base.widget.form

import android.text.TextUtils
import android.view.View
import androidx.annotation.StringRes

abstract class TextValidator(view: View) : AbsValidator(view) {

    override val isMatch: Boolean
        get() {
            val validateData = validateData
            if (TextUtils.isEmpty(validateData)) {
                handleNoMatch(validateView, emptyTips())
            } else {
                val result = validateTypeText(validateData)
                if (result != OK) {
                    handleNoMatch(validateView, noMatchTips(result))
                } else {
                    handleMatch(validateView)
                    return true
                }
            }
            return false
        }

    override val validateData: String
        get() = getStringData(validateView)

    protected open fun validateTypeText(content: String): Int {
        return if (simpleValidateTypeText(content)) {
            OK
        } else ILLEGAL
    }

    protected abstract fun simpleValidateTypeText(content: String): Boolean

    @StringRes
    internal abstract fun emptyTips(): Int

    @StringRes
    internal abstract fun noMatchTips(reason: Int): Int

    companion object {
        const val OK = 0
        const val ILLEGAL = 1
    }

}
