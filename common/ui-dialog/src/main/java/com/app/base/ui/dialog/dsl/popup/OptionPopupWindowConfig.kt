package com.app.base.ui.dialog.dsl.popup

import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.OnOptionSelectedListener
import com.app.base.ui.dialog.dsl.OptionList
import com.app.base.ui.dialog.dsl.PopupDim
import com.app.base.ui.dialog.dsl.Indicator
import com.app.base.ui.dialog.dsl.PopupWindowConfig

interface OptionPopupWindowConfig : PopupWindowConfig<OptionPopupWindowDescription> {

    fun list(config: OptionList.() -> Unit)

    fun options(list: List<CharSequence>, onOptionSelectedListener: OnOptionSelectedListener)

    fun customizeList(config: (RecyclerView) -> Unit)

    fun indicator(config: Indicator.() -> Unit)

}