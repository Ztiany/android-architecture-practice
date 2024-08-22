package com.app.base.ui.dialog.dsl.input

import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.DialogConfig
import com.app.base.ui.dialog.dsl.DialogContextDslMarker
import com.app.base.ui.dialog.dsl.DialogWindowSize
import com.app.base.ui.dialog.dsl.Field
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.Text

@DialogContextDslMarker
interface InputDialogConfig : DialogConfig<InputDialogBehavior, InputDialogDescription> {

    fun size(config: DialogWindowSize.() -> Unit)

    fun title(text: CharSequence, config: Text.() -> Unit = {})
    fun title(@StringRes textRes: Int, config: Text.() -> Unit = {})

    fun field(config: Field.() -> Unit = {})

    fun decorateFieldTop(config: InputDialogInterface.(ConstraintLayout) -> Unit)
    fun decorateFieldBottom(config: InputDialogInterface.(ConstraintLayout) -> Unit)

    fun positiveButton(text: CharSequence, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)
    fun positiveButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)

    fun negativeButton(text: CharSequence, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)
    fun negativeButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)

}

fun InputDialogConfig.forceInput() {
    behavior {
        forceInput(true)
    }
}

fun InputDialogConfig.autoShowKeyboard() {
    behavior {
        autoShowKeyboard(true)
    }
}