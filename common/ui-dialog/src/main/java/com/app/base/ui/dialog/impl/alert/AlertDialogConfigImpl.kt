package com.app.base.ui.dialog.impl.alert

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.base.ui.dialog.defaultAlertMessage
import com.app.base.ui.dialog.defaultAlertNegativeButton
import com.app.base.ui.dialog.defaultAlertNeutralButton
import com.app.base.ui.dialog.defaultAlertPositiveButton
import com.app.base.ui.dialog.defaultCheckbox
import com.app.base.ui.dialog.defaultDialogTitle
import com.app.base.ui.dialog.defaultWindowSize
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.CheckBox
import com.app.base.ui.dialog.dsl.DialogBehavior
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.Text
import com.app.base.ui.dialog.dsl.DialogWindowSize
import com.app.base.ui.dialog.dsl.alert.AlertDialogConfig
import com.app.base.ui.dialog.dsl.alert.AlertDialogDescription
import com.app.base.ui.dialog.dsl.alert.AlertDialogInterface

internal class AlertDialogConfigImpl(private val context: Context) : AlertDialogConfig {

    private var size = defaultWindowSize()

    private var title: Text? = null

    private var message: Text? = null

    private var messageConfig: (AlertDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var checkBox: CheckBox? = null

    private var positiveButton: Button = context.defaultAlertPositiveButton()

    private var negativeButton: Button? = null

    private var neutralButton: Button? = null

    private var behavior: DialogBehavior = DialogBehavior()

    override fun title(text: CharSequence, config: Text.() -> Unit) {
        title = context.defaultDialogTitle(text).apply(config)
    }

    override fun title(textRes: Int, config: Text.() -> Unit) {
        title(context.getText(textRes), config)
    }

    override fun message(text: CharSequence, config: Text.() -> Unit) {
        message = context.defaultAlertMessage(text).apply(config)
    }

    override fun message(textRes: Int, config: Text.() -> Unit) {
        message(context.getText(textRes), config)
    }

    override fun customizeMessage(config: AlertDialogInterface.(ConstraintLayout) -> Unit) {
        messageConfig = config
    }

    override fun checkBox(text: CharSequence, checked: Boolean, config: CheckBox.() -> Unit) {
        checkBox = context.defaultCheckbox(text, checked).apply(config)
    }

    override fun checkBox(textRes: Int, checked: Boolean, config: CheckBox.() -> Unit) {
        checkBox(context.getText(textRes), checked, config)
    }

    override fun positiveButton(text: CharSequence, config: Button.() -> Unit, onClickListener: OnClickListener?) {
        positiveButton = context.defaultAlertPositiveButton(text).apply {
            config()
            onClick(onClickListener)
        }
    }

    override fun positiveButton(textRes: Int, config: Button.() -> Unit, onClickListener: OnClickListener?) {
        positiveButton(context.getText(textRes), config, onClickListener)
    }

    override fun neutralButton(text: CharSequence, config: Button.() -> Unit, onClickListener: OnClickListener?) {
        neutralButton = context.defaultAlertNeutralButton(text).apply {
            config()
            onClick(onClickListener)
        }
    }

    override fun neutralButton(textRes: Int, config: Button.() -> Unit, onClickListener: OnClickListener?) {
        neutralButton(context.getText(textRes), config, onClickListener)
    }

    override fun negativeButton(text: CharSequence, config: Button.() -> Unit, onClickListener: OnClickListener?) {
        negativeButton = context.defaultAlertNegativeButton(text).apply {
            config()
            onClick(onClickListener)
        }
    }

    override fun negativeButton(textRes: Int, config: Button.() -> Unit, onClickListener: OnClickListener?) {
        negativeButton(context.getText(textRes), config, onClickListener)
    }

    override fun size(config: DialogWindowSize.() -> Unit) {
        size = size.apply(config)
    }

    override fun behavior(config: DialogBehavior.() -> Unit) {
        behavior = behavior.apply(config)
    }

    override fun toDescription(): AlertDialogDescription {
        return AlertDialogDescription(
            size = size.toDialogWindowSizeDescription(),
            title = title?.toTextDescription(),
            message = message?.toTextDescription(),
            checkBox = checkBox?.toCheckBoxDescription(),
            positiveButton = positiveButton.toButtonDescription(),
            neutralButton = neutralButton?.toButtonDescription(),
            negativeButton = negativeButton?.toButtonDescription(),
            behavior = behavior.toDialogBehaviorDescription(),
            messageConfig = messageConfig
        )
    }

}