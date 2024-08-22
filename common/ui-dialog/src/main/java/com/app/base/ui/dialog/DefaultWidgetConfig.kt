package com.app.base.ui.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
import android.view.Gravity
import androidx.annotation.ColorInt
import com.android.base.utils.android.views.dip
import com.app.base.ui.dialog.dsl.BottomSheetWindowSize
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.CheckBox
import com.app.base.ui.dialog.dsl.DialogWindowSize
import com.app.base.ui.dialog.dsl.DisplayList
import com.app.base.ui.dialog.dsl.Divider
import com.app.base.ui.dialog.dsl.Field
import com.app.base.ui.dialog.dsl.OptionList
import com.app.base.ui.dialog.dsl.PopupWindowSize
import com.app.base.ui.dialog.dsl.SelectionList
import com.app.base.ui.dialog.dsl.Text
import com.app.base.ui.dialog.dsl.TextStyle
import com.google.android.material.color.MaterialColors
import kotlin.math.roundToInt

///////////////////////////////////////////////////////////////////////////
// Common Window Size
///////////////////////////////////////////////////////////////////////////

internal fun defaultWindowSize(): DialogWindowSize {
    return DialogWindowSize(
        _width = {
            if (it.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                (0.75F * it.resources.displayMetrics.widthPixels).roundToInt()
            } else {
                (0.45F * it.resources.displayMetrics.widthPixels).roundToInt()
            }
        },
        _maxHeight = {
            if (it.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                (0.75F * it.resources.displayMetrics.heightPixels).roundToInt()
            } else {
                (0.9F * it.resources.displayMetrics.heightPixels).roundToInt()
            }
        }
    )
}

internal fun defaultBottomSheetWindowSize(): BottomSheetWindowSize {
    return BottomSheetWindowSize(
        _maxWidth = {
            if (it.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                it.resources.displayMetrics.widthPixels
            } else {
                (it.resources.displayMetrics.heightPixels * 0.9F).roundToInt()
            }
        }
    )
}

internal fun defaultOptionPopupWindowSize(): PopupWindowSize {
    return PopupWindowSize()
}

internal fun defaultSelectionPopupWindowSize(): PopupWindowSize {
    return PopupWindowSize().apply {
        fillMaxWidth()
    }
}

///////////////////////////////////////////////////////////////////////////
// Common Widgets Style
///////////////////////////////////////////////////////////////////////////

internal fun Context.defaultDivider(): Divider {
    return Divider(this).apply {
        colorAttr(com.app.base.ui.theme.R.attr.app_color_divider)
        thickness(1)
    }
}

internal fun Context.defaultDialogTitle(title: CharSequence): Text {
    return Text(this, title).apply {
        textSize(18F)
        boldText()
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_deepest)
        gravity(Gravity.CENTER)
    }
}

internal fun Context.defaultCheckbox(text: CharSequence, checked: Boolean): CheckBox {
    return CheckBox(this, text, checked).apply {
        textSize(14F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_text_level1)
        gravity(Gravity.CENTER)
    }
}

@ColorInt
internal fun Context.defaultDividerColor(): Int {
    return MaterialColors.getColor(this, com.app.base.ui.theme.R.attr.app_color_divider, "app_color_divider is not found in your theme.")
}

///////////////////////////////////////////////////////////////////////////
// Alert Dialog Widgets Style
///////////////////////////////////////////////////////////////////////////

internal fun Context.defaultAlertMessage(message: CharSequence = ""): Text {
    return Text(this, message).apply {
        textSize(16F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_text_level1)
        gravity(Gravity.CENTER)
    }
}

internal fun Context.defaultAlertPositiveButton(text: CharSequence): Button {
    val states = Array(2) { IntArray(1) }
    states[0][0] = -android.R.attr.state_enabled
    states[1][0] = 0

    val colorStateList = ColorStateList(
        states, intArrayOf(
            MaterialColors.getColor(this, com.app.base.ui.theme.R.attr.app_color_disable, "app_color_disable is not found in your theme."),
            MaterialColors.getColor(this, com.app.base.ui.theme.R.attr.app_color_main, "app_color_main is not found in your theme."),
        )
    )

    return Button(this, text).apply {
        textSize(14F)
        textColors(colorStateList)
        gravity(Gravity.CENTER)
    }
}

internal fun Context.defaultAlertNeutralButton(text: CharSequence): Button {
    return Button(this, text).apply {
        textSize(14F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_text_level2)
        gravity(Gravity.CENTER)
    }
}

internal fun Context.defaultAlertNegativeButton(text: CharSequence): Button {
    return Button(this, text).apply {
        textSize(14F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_text_level2)
        gravity(Gravity.CENTER)
    }
}

///////////////////////////////////////////////////////////////////////////
// Input Dialog Widgets Style
///////////////////////////////////////////////////////////////////////////

internal fun Context.defaultInputFiled(): Field {
    return Field(this, "").apply {
        textSize(12F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_text_level1)
        hintColorAttr(com.app.base.ui.theme.R.attr.app_color_text_level3)
        gravity(Gravity.START)
        backgroundColorAttr(android.R.attr.windowBackground)
        cornerSize(dip(8F))
        lines(1)
        inputType(TYPE_CLASS_TEXT or TYPE_TEXT_FLAG_MULTI_LINE)
    }
}

///////////////////////////////////////////////////////////////////////////
// Bottom Sheet Widgets Style
///////////////////////////////////////////////////////////////////////////

internal fun Context.defaultBottomSheetRightButton(text: CharSequence): Button {
    return Button(this, text).apply {
        textSize(14F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_lightest)
        gravity(Gravity.CENTER)
    }
}

internal fun Context.defaultBottomSheetTitleActionTextStyle(): TextStyle {
    return TextStyle(this).apply {
        textSize(14F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_main)
        gravity(Gravity.CENTER)
    }
}

internal fun Context.defaultDisplayList(): DisplayList {
    return DisplayList(
        this,
        _titleStyle = defaultDisplayListItemTitleStyle(),
        _subtitleStyle = defaultDisplayListItemTitleStyle(),
        _dividerColor = defaultDividerColor()
    )
}

internal fun Context.defaultDisplayListItemTitleStyle(): TextStyle {
    return TextStyle(this).apply {
        textSize(14F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_text_level1)
        gravity(Gravity.CENTER)
    }
}

internal fun Context.defaultDisplayListItemSubtitleStyle(): TextStyle {
    return TextStyle(this).apply {
        textSize(14F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_text_level2)
        gravity(Gravity.CENTER)
    }
}

internal fun Context.defaultSelectionList(): SelectionList {
    return SelectionList(
        this,
        _titleStyle = defaultSelectionListItemTitleStyle(),
        _subtitleStyle = defaultSelectionListItemSubtitleStyle(),
        _dividerColor = defaultDividerColor()
    )
}

internal fun Context.defaultSelectionListItemTitleStyle(): TextStyle {
    return TextStyle(this).apply {
        textSize(14F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_text_level1)
        gravity(Gravity.START)
    }
}

internal fun Context.defaultSelectionListItemSubtitleStyle(): TextStyle {
    return TextStyle(this).apply {
        textSize(14F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_text_level2)
        gravity(Gravity.START)
    }
}

///////////////////////////////////////////////////////////////////////////
// Option Popup Window Widget
///////////////////////////////////////////////////////////////////////////

internal fun Context.defaultOptionItemStyle(): TextStyle = defaultSelectionListItemTitleStyle().apply {
    gravity(Gravity.CENTER_VERTICAL)
}

internal fun Context.defaultOptionList(): OptionList {
    return OptionList(
        this,
        _itemStyle = defaultOptionItemStyle(),
    )
}