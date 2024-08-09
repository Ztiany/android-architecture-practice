package com.app.base.ui.dialog.impl.popup

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.defaultBottomSheetRightButton
import com.app.base.ui.dialog.defaultBottomSheetTitleActionTextStyle
import com.app.base.ui.dialog.defaultDialogTitle
import com.app.base.ui.dialog.defaultSelectionList
import com.app.base.ui.dialog.defaultSelectionPopupWindowSize
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.OnMultiItemSelectedListener
import com.app.base.ui.dialog.dsl.OnSingleItemSelectedListener
import com.app.base.ui.dialog.dsl.PopupDim
import com.app.base.ui.dialog.dsl.PopupWindowBehavior
import com.app.base.ui.dialog.dsl.PopupWindowSize
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.SelectionList
import com.app.base.ui.dialog.dsl.Text
import com.app.base.ui.dialog.dsl.TextStyle
import com.app.base.ui.dialog.dsl.popup.MultiSelectionPopupWindowConfig
import com.app.base.ui.dialog.dsl.popup.MultiSelectionPopupWindowDescription
import com.app.base.ui.dialog.dsl.popup.MultiSelectionPopupWindowInterface
import com.app.base.ui.dialog.dsl.popup.SelectionPopupWindowConfig
import com.app.base.ui.dialog.dsl.popup.SelectionPopupWindowDescription
import com.app.base.ui.dialog.dsl.popup.SingleSelectionPopupWindowConfig
import com.app.base.ui.dialog.dsl.popup.SingleSelectionPopupWindowDescription
import com.app.base.ui.dialog.dsl.popup.SingleSelectionPopupWindowInterface

internal abstract class SelectionPopupWindowConfigImpl<Description : SelectionPopupWindowDescription>(
    private val context: Context,
) : SelectionPopupWindowConfig<Description> {

    protected var behavior = PopupWindowBehavior()

    protected var size = defaultSelectionPopupWindowSize()

    protected var popupDim: PopupDim = PopupDim(context)

    protected var title: Text? = null

    protected var selectionList: SelectionList? = null

    protected var bottomButton: Button? = null

    override fun behavior(config: PopupWindowBehavior.() -> Unit) {
        behavior = behavior.apply(config)
    }

    override fun size(config: PopupWindowSize.() -> Unit) {
        size = size.apply(config)
    }

    override fun dimStyle(config: PopupDim.() -> Unit) {
        popupDim = PopupDim(context).apply(config)
    }

    override fun title(text: CharSequence, config: Text.() -> Unit) {
        title = context.defaultDialogTitle(text).apply(config)
    }

    override fun title(textRes: Int, config: Text.() -> Unit) {
        title(context.getString(textRes), config)
    }

    override fun list(config: SelectionList.() -> Unit) {
        selectionList = context.defaultSelectionList().apply(config)
    }

    override fun selections(list: List<Selection>) {
        selectionList = context.defaultSelectionList().apply {
            selections(list)
        }
    }

}

internal class SingleSelectionPopupWindowConfigImpl(
    private val context: Context,
) : SelectionPopupWindowConfigImpl<SingleSelectionPopupWindowDescription>(context), SingleSelectionPopupWindowConfig {

    private var _onSingleItemSelectedListener: OnSingleItemSelectedListener? = null

    private var listTopAreaConfig: (SingleSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)? = null

    private var listBottomAreaConfig: (SingleSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)? = null

    private var customizeList: (SingleSelectionPopupWindowInterface.(RecyclerView) -> Unit)? = null

    override fun positiveButton(text: CharSequence, config: Button.() -> Unit, onSelected: OnSingleItemSelectedListener) {
        _onSingleItemSelectedListener = onSelected
        bottomButton = context.defaultBottomSheetRightButton(text).apply {
            config(this)
        }
    }

    override fun positiveButton(textRes: Int, config: Button.() -> Unit, onSelected: OnSingleItemSelectedListener) {
        positiveButton(context.getString(textRes), config, onSelected)
    }

    override fun decorateListTop(config: SingleSelectionPopupWindowInterface.(ConstraintLayout) -> Unit) {
        listTopAreaConfig = config
    }

    override fun decorateListBottom(config: SingleSelectionPopupWindowInterface.(ConstraintLayout) -> Unit) {
        listBottomAreaConfig = config
    }

    override fun customizeList(config: SingleSelectionPopupWindowInterface.(RecyclerView) -> Unit) {
        customizeList = config
    }

    override fun toDescription(): SingleSelectionPopupWindowDescription {
        return SingleSelectionPopupWindowDescription(
            behavior.toPopupWindowBehaviorDescription(),
            size = size.toWindowSizeDescription(),
            popupDim = popupDim.toPopupDimDescription(),
            title = title?.toTextDescription(),
            listTopAreaConfig = listTopAreaConfig,
            listBottomAreaConfig = listBottomAreaConfig,
            customizeList = customizeList,
            list = selectionList?.toSelectionListDescription(),
            bottomButton = bottomButton?.toButtonDescription(),
            onSingleItemSelectedListener = _onSingleItemSelectedListener,
        )
    }

}

internal class MultiSelectionPopupConfigImpl(
    private val context: Context,
) : SelectionPopupWindowConfigImpl<MultiSelectionPopupWindowDescription>(context), MultiSelectionPopupWindowConfig {

    private var _onMultiItemSelectedListener: OnMultiItemSelectedListener? = null

    private var _rightActionTextStyle: TextStyle = context.defaultBottomSheetTitleActionTextStyle()

    private var listTopAreaConfig: (MultiSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)? = null

    private var listBottomAreaConfig: (MultiSelectionPopupWindowInterface.(ConstraintLayout) -> Unit)? = null

    private var customizeList: (MultiSelectionPopupWindowInterface.(RecyclerView) -> Unit)? = null

    override fun positiveButton(text: CharSequence, config: Button.() -> Unit, onSelected: OnMultiItemSelectedListener) {
        _onMultiItemSelectedListener = onSelected
        bottomButton = context.defaultBottomSheetRightButton(text).apply {
            config(this)
        }
    }

    override fun positiveButton(textRes: Int, config: Button.() -> Unit, onSelected: OnMultiItemSelectedListener) {
        positiveButton(context.getString(textRes), config, onSelected)
    }

    override fun decorateListTop(config: MultiSelectionPopupWindowInterface.(ConstraintLayout) -> Unit) {
        listTopAreaConfig = config
    }

    override fun decorateListBottom(config: MultiSelectionPopupWindowInterface.(ConstraintLayout) -> Unit) {
        listBottomAreaConfig = config
    }

    override fun customizeList(config: MultiSelectionPopupWindowInterface.(RecyclerView) -> Unit) {
        customizeList = config
    }

    override fun toDescription(): MultiSelectionPopupWindowDescription {
        return MultiSelectionPopupWindowDescription(
            behavior.toPopupWindowBehaviorDescription(),
            size = size.toWindowSizeDescription(),
            popupDim = popupDim.toPopupDimDescription(),
            title = title?.toTextDescription(),
            listTopAreaConfig = listTopAreaConfig,
            listBottomAreaConfig = listBottomAreaConfig,
            customizeList = customizeList,
            list = selectionList?.toSelectionListDescription(),
            bottomButton = bottomButton?.toButtonDescription(),
            rightTitleActionTextStyle = _rightActionTextStyle.toTextStyleDescription(),
            onMultiItemSelectedListener = _onMultiItemSelectedListener,
        )
    }

}