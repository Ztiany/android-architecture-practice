package com.app.base.widget.form

import android.view.View

abstract class AbsValidator(val validateView: View) : IValidator {

    abstract val isMatch: Boolean

    abstract val validateData: Any

    override fun validate(): Boolean {
        return isMatch
    }

}
