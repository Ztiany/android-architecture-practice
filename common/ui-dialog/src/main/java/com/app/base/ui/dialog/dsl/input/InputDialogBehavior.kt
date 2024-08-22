package com.app.base.ui.dialog.dsl.input

import com.app.base.ui.dialog.dsl.DialogBehavior
import com.app.base.ui.dialog.dsl.DialogBehaviorDescription

class InputDialogBehavior : DialogBehavior() {

    private var _forceInput: Boolean = false

    private var _showSoftInputAfterShow: Boolean = false

    fun forceInput(value: Boolean) {
        _forceInput = value
    }

    fun autoShowKeyboard(value: Boolean) {
        _showSoftInputAfterShow = value
    }

    fun toInputDialogBehaviorDescription(): InputDialogBehaviorDescription {
        return InputDialogBehaviorDescription(
            forceInput = _forceInput,
            showSoftInputAfterShow = _showSoftInputAfterShow,
            dialogBehavior = toDialogBehaviorDescription()
        )
    }

}

class InputDialogBehaviorDescription(
    val forceInput: Boolean,
    val showSoftInputAfterShow: Boolean,
    val dialogBehavior: DialogBehaviorDescription,
)