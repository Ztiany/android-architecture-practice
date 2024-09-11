package com.app.base.ui.dialog.impl.bottomsheet

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.defaultBottomSheetPositiveButton
import com.app.base.ui.dialog.defaultBottomSheetWindowSize
import com.app.base.ui.dialog.defaultDialogTitle
import com.app.base.ui.dialog.defaultDisplayList
import com.app.base.ui.dialog.dsl.BottomSheetWindowSize
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.DisplayItem
import com.app.base.ui.dialog.dsl.DisplayList
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.OnItemClickListener
import com.app.base.ui.dialog.dsl.Text
import com.app.base.ui.dialog.dsl.bottomsheet.BottomSheetDialogBehavior
import com.app.base.ui.dialog.dsl.bottomsheet.ListBottomSheetDialogConfig
import com.app.base.ui.dialog.dsl.bottomsheet.ListBottomSheetDialogDescription
import com.app.base.ui.dialog.dsl.bottomsheet.ListBottomSheetDialogInterface

internal class ListBottomSheetDialogConfigImpl(private val context: Context) : ListBottomSheetDialogConfig {

    private var size: BottomSheetWindowSize = defaultBottomSheetWindowSize()

    private var title: Text? = null

    private var listTopAreaConfig: (ListBottomSheetDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var listBottomAreaConfig: (ListBottomSheetDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var customizeList: (ListBottomSheetDialogInterface.(RecyclerView) -> Unit)? = null

    private var displayList: DisplayList? = null

    private var behavior: BottomSheetDialogBehavior = BottomSheetDialogBehavior()

    private var bottomButton: Button? = null

    override fun size(config: BottomSheetWindowSize.() -> Unit) {
        size = size.apply(config)
    }

    override fun title(text: CharSequence, config: Text.() -> Unit) {
        title = context.defaultDialogTitle(text).apply(config)
    }

    override fun title(textRes: Int, config: Text.() -> Unit) {
        title(context.getString(textRes), config)
    }

    override fun decorateListTop(config: ListBottomSheetDialogInterface.(ConstraintLayout) -> Unit) {
        listTopAreaConfig = config
    }

    override fun decorateListBottom(config: ListBottomSheetDialogInterface.(ConstraintLayout) -> Unit) {
        listBottomAreaConfig = config
    }

    override fun list(config: DisplayList.() -> Unit) {
        displayList = context.defaultDisplayList().apply(config)
    }

    override fun items(list: List<DisplayItem>, onItemClickListener: OnItemClickListener?) {
        displayList = context.defaultDisplayList().apply {
            items(list)
            onItemClickListener?.let { onItemClicked(it) }
        }
    }

    override fun customizeList(config: ListBottomSheetDialogInterface.(RecyclerView) -> Unit) {
        customizeList = config
    }

    override fun bottomButton(text: CharSequence, config: Button.() -> Unit, onClickListener: OnClickListener?) {
        bottomButton = context.defaultBottomSheetPositiveButton(text).apply {
            onClick(onClickListener)
            config(this)
        }
    }

    override fun bottomButton(textRes: Int, config: Button.() -> Unit, onClickListener: OnClickListener?) {
        bottomButton(context.getText(textRes), config, onClickListener)
    }

    override fun behavior(config: BottomSheetDialogBehavior.() -> Unit) {
        behavior = behavior.apply(config)
    }

    override fun toDescription(): ListBottomSheetDialogDescription {
        return ListBottomSheetDialogDescription(
            size = size.toBottomSheetWindowSizeDescription(),
            title = title?.toTextDescription(),
            listTopAreaConfig = listTopAreaConfig,
            listBottomAreaConfig = listBottomAreaConfig,
            customizeList = customizeList,
            listDescription = displayList?.toDisplayListDescription(),
            behavior = behavior.toBottomSheetDialogBehaviorDescription(),
            bottomButton = bottomButton?.toButtonDescription(),
        )
    }

}