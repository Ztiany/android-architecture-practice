package com.app.base.ui.dialog.impl.list

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.defaultAlertNegativeButton
import com.app.base.ui.dialog.defaultDialogTitle
import com.app.base.ui.dialog.defaultListNegativeButton
import com.app.base.ui.dialog.defaultListPositiveButton
import com.app.base.ui.dialog.defaultListTitleActionTextStyle
import com.app.base.ui.dialog.defaultSelectionList
import com.app.base.ui.dialog.defaultWindowSize
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.DialogBehavior
import com.app.base.ui.dialog.dsl.DialogWindowSize
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.OnMultiSelectionSelectedListener
import com.app.base.ui.dialog.dsl.OnSingleSelectionSelectedListener
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.SelectionList
import com.app.base.ui.dialog.dsl.Text
import com.app.base.ui.dialog.dsl.TextStyle
import com.app.base.ui.dialog.dsl.list.MultiSelectionListDialogConfig
import com.app.base.ui.dialog.dsl.list.MultiSelectionListDialogDescription
import com.app.base.ui.dialog.dsl.list.MultiSelectionListDialogInterface
import com.app.base.ui.dialog.dsl.list.SelectionListDialogConfig
import com.app.base.ui.dialog.dsl.list.SelectionListDialogDescription
import com.app.base.ui.dialog.dsl.list.SingleSelectionListDialogConfig
import com.app.base.ui.dialog.dsl.list.SingleSelectionListDialogDescription
import com.app.base.ui.dialog.dsl.list.SingleSelectionListDialogInterface

internal abstract class SelectionListDialogConfigImpl<Description : SelectionListDialogDescription>(
    private val context: Context,
) : SelectionListDialogConfig<Description> {

    protected var size = defaultWindowSize()

    protected var title: Text? = null

    protected var selectionList: SelectionList? = null

    protected var behavior: DialogBehavior = DialogBehavior()

    protected var bottomButton: Button? = null

    protected var negativeButton: Button? = null

    override fun size(config: DialogWindowSize.() -> Unit) {
        size = size.apply(config)
    }

    override fun title(text: CharSequence, config: Text.() -> Unit) {
        title = context.defaultDialogTitle(text).apply(config)
    }

    override fun title(textRes: Int, config: Text.() -> Unit) {
        title(context.getString(textRes), config)
    }

    override fun behavior(config: DialogBehavior.() -> Unit) {
        behavior = behavior.apply(config)
    }

    override fun list(config: SelectionList.() -> Unit) {
        selectionList = context.defaultSelectionList().apply(config)
    }

    override fun selections(list: List<Selection>) {
        selectionList = context.defaultSelectionList().apply {
            selections(list)
        }
    }

    override fun negativeButton(text: CharSequence, config: Button.() -> Unit, onClickListener: OnClickListener?) {
        negativeButton = context.defaultListNegativeButton(text).apply {
            config()
            onClick(onClickListener)
        }
    }

    override fun negativeButton(textRes: Int, config: Button.() -> Unit, onClickListener: OnClickListener?) {
        negativeButton(context.getText(textRes), config, onClickListener)
    }

}

internal class SingleSelectionListDialogConfigImpl(
    private val context: Context,
) : SelectionListDialogConfigImpl<SingleSelectionListDialogDescription>(context), SingleSelectionListDialogConfig {

    private var _onSingleItemSelectedListener: OnSingleSelectionSelectedListener? = null

    private var listTopAreaConfig: (SingleSelectionListDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var listBottomAreaConfig: (SingleSelectionListDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var customizeList: (SingleSelectionListDialogInterface.(RecyclerView) -> Unit)? = null

    override fun positiveButton(text: CharSequence, config: Button.() -> Unit, onSelected: OnSingleSelectionSelectedListener) {
        _onSingleItemSelectedListener = onSelected
        bottomButton = context.defaultListPositiveButton(text).apply {
            config(this)
        }
    }

    override fun positiveButton(textRes: Int, config: Button.() -> Unit, onSelected: OnSingleSelectionSelectedListener) {
        positiveButton(context.getString(textRes), config, onSelected)
    }

    override fun decorateListTop(config: SingleSelectionListDialogInterface.(ConstraintLayout) -> Unit) {
        listTopAreaConfig = config
    }

    override fun decorateListBottom(config: SingleSelectionListDialogInterface.(ConstraintLayout) -> Unit) {
        listBottomAreaConfig = config
    }

    override fun customizeList(config: SingleSelectionListDialogInterface.(RecyclerView) -> Unit) {
        customizeList = config
    }

    override fun toDescription(): SingleSelectionListDialogDescription {
        return SingleSelectionListDialogDescription(
            size = size.toDialogWindowSizeDescription(),
            title = title?.toTextDescription(),
            listTopAreaConfig = listTopAreaConfig,
            listBottomAreaConfig = listBottomAreaConfig,
            customizeList = customizeList,
            list = selectionList?.toSelectionListDescription(),
            behavior = behavior.toDialogBehaviorDescription(),
            negativeButton = negativeButton?.toButtonDescription(),
            positiveButton = bottomButton?.toButtonDescription(),
            onSingleItemSelectedListener = _onSingleItemSelectedListener,
        )
    }

}

internal class MultiSelectionListDialogConfigImpl(
    private val context: Context,
) : SelectionListDialogConfigImpl<MultiSelectionListDialogDescription>(context), MultiSelectionListDialogConfig {

    private var _onMultiItemSelectedListener: OnMultiSelectionSelectedListener? = null

    private var _rightActionTextStyle: TextStyle = context.defaultListTitleActionTextStyle()

    private var listTopAreaConfig: (MultiSelectionListDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var listBottomAreaConfig: (MultiSelectionListDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var customizeList: (MultiSelectionListDialogInterface.(RecyclerView) -> Unit)? = null

    override fun positiveButton(text: CharSequence, config: Button.() -> Unit, onSelected: OnMultiSelectionSelectedListener) {
        _onMultiItemSelectedListener = onSelected
        bottomButton = context.defaultListPositiveButton(text).apply {
            config(this)
        }
    }

    override fun positiveButton(textRes: Int, config: Button.() -> Unit, onSelected: OnMultiSelectionSelectedListener) {
        positiveButton(context.getString(textRes), config, onSelected)
    }

    override fun decorateListTop(config: MultiSelectionListDialogInterface.(ConstraintLayout) -> Unit) {
        listTopAreaConfig = config
    }

    override fun decorateListBottom(config: MultiSelectionListDialogInterface.(ConstraintLayout) -> Unit) {
        listBottomAreaConfig = config
    }

    override fun customizeList(config: MultiSelectionListDialogInterface.(RecyclerView) -> Unit) {
        customizeList = config
    }

    override fun toDescription(): SelectionListDialogDescription {
        return MultiSelectionListDialogDescription(
            size = size.toDialogWindowSizeDescription(),
            title = title?.toTextDescription(),
            listTopAreaConfig = listTopAreaConfig,
            listBottomAreaConfig = listBottomAreaConfig,
            customizeList = customizeList,
            list = selectionList?.toSelectionListDescription(),
            behavior = behavior.toDialogBehaviorDescription(),
            negativeButton = negativeButton?.toButtonDescription(),
            positiveButton = bottomButton?.toButtonDescription(),
            rightTitleActionTextStyle = _rightActionTextStyle.toTextStyleDescription(),
            onMultiItemSelectedListener = _onMultiItemSelectedListener,
        )
    }

}