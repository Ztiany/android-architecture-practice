package com.app.base.ui.dialog.dsl

interface Condition {

    fun isConditionMeet(id: Int = DEFAULT): Boolean

    companion object {
        const val DEFAULT = 0
    }

}