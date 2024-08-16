package com.app.base.ui.dialog

import android.content.Context
import android.content.res.Configuration
import android.view.Gravity
import androidx.annotation.ColorInt
import com.app.base.ui.dialog.dsl.BottomSheetWindowSize
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.CheckBox
import com.app.base.ui.dialog.dsl.DialogWindowSize
import com.app.base.ui.dialog.dsl.DisplayList
import com.app.base.ui.dialog.dsl.Divider
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
    return Button(this, text).apply {
        textSize(14F)
        textColorAttr(com.app.base.ui.theme.R.attr.app_color_main)
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

internal fun Context.defaultOptionItemStyle(): TextStyle = defaultSelectionListItemTitleStyle()

internal fun Context.defaultOptionList(): OptionList {
    return OptionList(
        this,
        _itemStyle = defaultOptionItemStyle(),
    )
}