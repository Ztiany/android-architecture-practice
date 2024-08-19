package com.app.base.ui.dialog.impl.bottomsheet

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.defaultBottomSheetRightButton
import com.app.base.ui.dialog.defaultBottomSheetTitleActionTextStyle
import com.app.base.ui.dialog.defaultBottomSheetWindowSize
import com.app.base.ui.dialog.defaultDialogTitle
import com.app.base.ui.dialog.defaultSelectionList
import com.app.base.ui.dialog.dsl.BottomSheetWindowSize
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.OnMultiSelectionSelectedListener
import com.app.base.ui.dialog.dsl.OnSingleSelectionSelectedListener
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.SelectionList
import com.app.base.ui.dialog.dsl.Text
import com.app.base.ui.dialog.dsl.TextStyle
import com.app.base.ui.dialog.dsl.bottomsheet.BottomSheetDialogBehavior
import com.app.base.ui.dialog.dsl.bottomsheet.MultiSectionBottomSheetDialogDescription
import com.app.base.ui.dialog.dsl.bottomsheet.MultiSelectionBottomSheetDialogConfig
import com.app.base.ui.dialog.dsl.bottomsheet.MultiSelectionBottomSheetDialogInterface
import com.app.base.ui.dialog.dsl.bottomsheet.SectionBottomSheetDialogDescription
import com.app.base.ui.dialog.dsl.bottomsheet.SelectionBottomSheetDialogConfig
import com.app.base.ui.dialog.dsl.bottomsheet.SingleSectionBottomSheetDialogDescription
import com.app.base.ui.dialog.dsl.bottomsheet.SingleSelectionBottomSheetDialogConfig
import com.app.base.ui.dialog.dsl.bottomsheet.SingleSelectionBottomSheetDialogInterface

internal abstract class SelectionBottomSheetDialogConfigImpl<Description : SectionBottomSheetDialogDescription>(
    private val context: Context,
) : SelectionBottomSheetDialogConfig<Description> {

    protected var size: BottomSheetWindowSize = defaultBottomSheetWindowSize()

    protected var title: Text? = null

    protected var selectionList: SelectionList? = null

    protected var behavior: BottomSheetDialogBehavior = BottomSheetDialogBehavior()

    protected var bottomButton: Button? = null

    override fun size(config: BottomSheetWindowSize.() -> Unit) {
        size = size.apply(config)
    }

    override fun title(text: CharSequence, config: Text.() -> Unit) {
        title = context.defaultDialogTitle(text).apply(config)
    }

    override fun title(textRes: Int, config: Text.() -> Unit) {
        title(context.getString(textRes), config)
    }

    override fun behavior(config: BottomSheetDialogBehavior.() -> Unit) {
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

}

internal class SingleSelectionBottomSheetDialogConfigImpl(
    private val context: Context,
) : SelectionBottomSheetDialogConfigImpl<SingleSectionBottomSheetDialogDescription>(context), SingleSelectionBottomSheetDialogConfig {

    private var _onSingleItemSelectedListener: OnSingleSelectionSelectedListener? = null

    private var listTopAreaConfig: (SingleSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var listBottomAreaConfig: (SingleSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var customizeList: (SingleSelectionBottomSheetDialogInterface.(RecyclerView) -> Unit)? = null

    override fun positiveButton(text: CharSequence, config: Button.() -> Unit, onSelected: OnSingleSelectionSelectedListener) {
        _onSingleItemSelectedListener = onSelected
        bottomButton = context.defaultBottomSheetRightButton(text).apply {
            config(this)
        }
    }

    override fun positiveButton(textRes: Int, config: Button.() -> Unit, onSelected: OnSingleSelectionSelectedListener) {
        positiveButton(context.getString(textRes), config, onSelected)
    }

    override fun decorateListTop(config: SingleSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit) {
        listTopAreaConfig = config
    }

    override fun decorateListBottom(config: SingleSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit) {
        listBottomAreaConfig = config
    }

    override fun customizeList(config: SingleSelectionBottomSheetDialogInterface.(RecyclerView) -> Unit) {
        customizeList = config
    }

    override fun toDescription(): SingleSectionBottomSheetDialogDescription {
        return SingleSectionBottomSheetDialogDescription(
            size = size.toBottomSheetWindowSizeDescription(),
            title = title?.toTextDescription(),
            listTopAreaConfig = listTopAreaConfig,
            listBottomAreaConfig = listBottomAreaConfig,
            customizeList = customizeList,
            list = selectionList?.toSelectionListDescription(),
            behavior = behavior.toBottomSheetDialogBehaviorDescription(),
            positiveButton = bottomButton?.toButtonDescription(),
            onSingleItemSelectedListener = _onSingleItemSelectedListener,
        )
    }

}

internal class MultiSelectionBottomSheetDialogConfigImpl(
    private val context: Context,
) : SelectionBottomSheetDialogConfigImpl<MultiSectionBottomSheetDialogDescription>(context), MultiSelectionBottomSheetDialogConfig {

    private var _onMultiItemSelectedListener: OnMultiSelectionSelectedListener? = null

    private var _rightActionTextStyle: TextStyle = context.defaultBottomSheetTitleActionTextStyle()

    private var listTopAreaConfig: (MultiSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var listBottomAreaConfig: (MultiSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var customizeList: (MultiSelectionBottomSheetDialogInterface.(RecyclerView) -> Unit)? = null

    override fun positiveButton(text: CharSequence, config: Button.() -> Unit, onSelected: OnMultiSelectionSelectedListener) {
        _onMultiItemSelectedListener = onSelected
        bottomButton = context.defaultBottomSheetRightButton(text).apply {
            config(this)
        }
    }

    override fun positiveButton(textRes: Int, config: Button.() -> Unit, onSelected: OnMultiSelectionSelectedListener) {
        positiveButton(context.getString(textRes), config, onSelected)
    }

    override fun decorateListTop(config: MultiSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit) {
        listTopAreaConfig = config
    }

    override fun decorateListBottom(config: MultiSelectionBottomSheetDialogInterface.(ConstraintLayout) -> Unit) {
        listBottomAreaConfig = config
    }

    override fun customizeList(config: MultiSelectionBottomSheetDialogInterface.(RecyclerView) -> Unit) {
        customizeList = config
    }

    override fun toDescription(): SectionBottomSheetDialogDescription {
        return MultiSectionBottomSheetDialogDescription(
            size = size.toBottomSheetWindowSizeDescription(),
            title = title?.toTextDescription(),
            listTopAreaConfig = listTopAreaConfig,
            listBottomAreaConfig = listBottomAreaConfig,
            customizeList = customizeList,
            list = selectionList?.toSelectionListDescription(),
            behavior = behavior.toBottomSheetDialogBehaviorDescription(),
            positiveButton = bottomButton?.toButtonDescription(),
            rightTitleActionTextStyle = _rightActionTextStyle.toTextStyleDescription(),
            onMultiItemSelectedListener = _onMultiItemSelectedListener,
        )
    }

}