package com.app.base.ui.dialog.impl.input

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.base.ui.dialog.defaultAlertNegativeButton
import com.app.base.ui.dialog.defaultAlertPositiveButton
import com.app.base.ui.dialog.defaultDialogTitle
import com.app.base.ui.dialog.defaultInputFiled
import com.app.base.ui.dialog.defaultWindowSize
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.DialogWindowSize
import com.app.base.ui.dialog.dsl.Field
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.Text
import com.app.base.ui.dialog.dsl.input.InputDialogBehavior
import com.app.base.ui.dialog.dsl.input.InputDialogConfig
import com.app.base.ui.dialog.dsl.input.InputDialogDescription
import com.app.base.ui.dialog.dsl.input.InputDialogInterface

internal class InputDialogConfigImpl(private val context: Context) : InputDialogConfig {

    private var size = defaultWindowSize()

    private var title: Text? = null

    private var field = context.defaultInputFiled()

    private var positiveButton: Button? = null

    private var negativeButton: Button? = null

    private var behavior: InputDialogBehavior = InputDialogBehavior().apply {
        cancelOnTouchOutside(false)
    }

    private var fieldTopAreaConfig: (InputDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var fieldBottomAreaConfig: (InputDialogInterface.(ConstraintLayout) -> Unit)? = null

    override fun title(text: CharSequence, config: Text.() -> Unit) {
        title = context.defaultDialogTitle(text).apply(config)
    }

    override fun title(textRes: Int, config: Text.() -> Unit) {
        title(context.getText(textRes), config)
    }

    override fun field(config: Field.() -> Unit) {
        field = field.apply(config)
    }

    override fun decorateFieldTop(config: InputDialogInterface.(ConstraintLayout) -> Unit) {
        fieldTopAreaConfig = config
    }

    override fun decorateFieldBottom(config: InputDialogInterface.(ConstraintLayout) -> Unit) {
        fieldBottomAreaConfig = config
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

    override fun behavior(config: InputDialogBehavior.() -> Unit) {
        behavior = behavior.apply(config)
    }

    override fun toDescription(): InputDialogDescription {
        return InputDialogDescription(
            size = size.toDialogWindowSizeDescription(),
            title = title?.toTextDescription(),
            filed = field.toFieldDescription(),
            positiveButton = positiveButton?.toButtonDescription(),
            negativeButton = negativeButton?.toButtonDescription(),
            behavior = behavior.toInputDialogBehaviorDescription(),
            fieldTopAreaConfig = fieldTopAreaConfig,
            fieldBottomAreaConfig = fieldBottomAreaConfig
        )
    }

}