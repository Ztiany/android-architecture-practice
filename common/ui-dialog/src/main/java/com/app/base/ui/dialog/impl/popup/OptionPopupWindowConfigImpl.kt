package com.app.base.ui.dialog.impl.popup

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.defaultOptionList
import com.app.base.ui.dialog.defaultOptionPopupWindowSize
import com.app.base.ui.dialog.dsl.Indicator
import com.app.base.ui.dialog.dsl.OnOptionSelectedListener
import com.app.base.ui.dialog.dsl.OptionList
import com.app.base.ui.dialog.dsl.PopupDim
import com.app.base.ui.dialog.dsl.PopupWindowBehavior
import com.app.base.ui.dialog.dsl.PopupWindowSize
import com.app.base.ui.dialog.dsl.popup.OptionPopupWindowConfig
import com.app.base.ui.dialog.dsl.popup.OptionPopupWindowDescription

class OptionPopupWindowConfigImpl(private val context: Context) : OptionPopupWindowConfig {

    private var behavior = PopupWindowBehavior()

    private var size = defaultOptionPopupWindowSize()

    private var displayList: OptionList? = null

    private var customizeList: ((RecyclerView) -> Unit)? = null

    private var popupDim: PopupDim = PopupDim(context)

    private var indicator: Indicator = Indicator()

    override fun list(config: OptionList.() -> Unit) {
        displayList = context.defaultOptionList().apply(config)
    }

    override fun options(list: List<CharSequence>, onOptionSelectedListener: OnOptionSelectedListener) {
        displayList = context.defaultOptionList().apply {
            items(list)
            onItemClick(onOptionSelectedListener)
        }
    }

    override fun customizeList(config: (RecyclerView) -> Unit) {
        customizeList = config
    }

    override fun dimStyle(config: PopupDim.() -> Unit) {
        popupDim = PopupDim(context).apply(config)
    }

    override fun behavior(config: PopupWindowBehavior.() -> Unit) {
        behavior = behavior.apply(config)
    }

    override fun indicator(config: Indicator.() -> Unit) {
        indicator = Indicator().apply(config)
    }

    override fun size(config: PopupWindowSize.() -> Unit) {
        size = size.apply(config)
    }

    override fun toDescription(): OptionPopupWindowDescription {
        return OptionPopupWindowDescription(
            behavior = behavior.toPopupWindowBehaviorDescription(),
            size = size.toWindowSizeDescription(),
            optionList = displayList?.toOptionListDescription(),
            customizeList = customizeList,
            popupDim = popupDim.toPopupDimDescription(),
            indicator = indicator.toPopupIndicatorDescription()
        )
    }

}