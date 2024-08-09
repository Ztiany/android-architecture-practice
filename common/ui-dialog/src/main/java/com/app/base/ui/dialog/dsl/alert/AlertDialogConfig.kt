package com.app.base.ui.dialog.dsl.alert

import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.CheckBox
import com.app.base.ui.dialog.dsl.DialogBehavior
import com.app.base.ui.dialog.dsl.DialogConfig
import com.app.base.ui.dialog.dsl.DialogContextDslMarker
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.Text
import com.app.base.ui.dialog.dsl.DialogWindowSize

@DialogContextDslMarker
interface AlertDialogConfig : DialogConfig<DialogBehavior, AlertDialogDescription> {

    fun size(config: DialogWindowSize.() -> Unit)

    fun title(text: CharSequence, config: Text.() -> Unit = {})
    fun title(@StringRes textRes: Int, config: Text.() -> Unit = {})

    fun message(text: CharSequence, config: Text.() -> Unit = {})
    fun message(@StringRes textRes: Int, config: Text.() -> Unit = {})
    fun customizeMessage(config: AlertDialogInterface.(ConstraintLayout) -> Unit)

    fun checkBox(text: CharSequence, checked: Boolean, config: CheckBox.() -> Unit = {})
    fun checkBox(@StringRes textRes: Int, checked: Boolean, config: CheckBox.() -> Unit = {})

    fun positiveButton(text: CharSequence, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)
    fun positiveButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)

    fun neutralButton(text: CharSequence, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)
    fun neutralButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)

    fun negativeButton(text: CharSequence, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)
    fun negativeButton(@StringRes textRes: Int, config: Button.() -> Unit = {}, onClickListener: OnClickListener? = null)

}