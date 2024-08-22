package com.app.base.ui.dialog.dsl

interface Condition {

    fun isConditionMeet(id: Int = CHECKBOX): Boolean = false

    fun getFieldValue(id: Int = FIELD): CharSequence = ""

    companion object {
        const val CHECKBOX = 1
        const val FIELD = 2
    }

}

