package com.app.base.ui.dialog.dsl.popup

import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.IndicatorDescription
import com.app.base.ui.dialog.dsl.OptionListDescription
import com.app.base.ui.dialog.dsl.PopupDimDescription
import com.app.base.ui.dialog.dsl.PopupWindowBehaviorDescription
import com.app.base.ui.dialog.dsl.PopupWindowDescription
import com.app.base.ui.dialog.dsl.PopupWindowSizeDescription

class OptionPopupWindowDescription(
    val behavior: PopupWindowBehaviorDescription,
    val size: PopupWindowSizeDescription,
    val optionList: OptionListDescription?,
    val popupDim: PopupDimDescription?,
    val indicator: IndicatorDescription,
    val customizeList: (RecyclerView.() -> Unit)?,
) : PopupWindowDescription