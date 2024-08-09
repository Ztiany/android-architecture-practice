package com.app.base.ui.dialog.impl.list

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.defaultAlertNegativeButton
import com.app.base.ui.dialog.defaultAlertPositiveButton
import com.app.base.ui.dialog.defaultDialogTitle
import com.app.base.ui.dialog.defaultDisplayList
import com.app.base.ui.dialog.defaultWindowSize
import com.app.base.ui.dialog.dsl.Button
import com.app.base.ui.dialog.dsl.DialogBehavior
import com.app.base.ui.dialog.dsl.DisplayList
import com.app.base.ui.dialog.dsl.DisplayItem
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.OnItemClickListener
import com.app.base.ui.dialog.dsl.Text
import com.app.base.ui.dialog.dsl.DialogWindowSize
import com.app.base.ui.dialog.dsl.list.ListDialogConfig
import com.app.base.ui.dialog.dsl.list.ListDialogDescription
import com.app.base.ui.dialog.dsl.list.ListDialogInterface

internal class ListDialogConfigImpl(private val context: Context) : ListDialogConfig {

    private var size = defaultWindowSize()

    private var title: Text? = null

    private var positiveButton: Button? = null

    private var negativeButton: Button? = null

    private var listTopAreaConfig: (ListDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var listBottomAreaConfig: (ListDialogInterface.(ConstraintLayout) -> Unit)? = null

    private var customizeList: (ListDialogInterface.(RecyclerView) -> Unit)? = null

    private var displayList: DisplayList? = null

    private var behavior: DialogBehavior = DialogBehavior()

    override fun title(text: CharSequence, config: Text.() -> Unit) {
        title = context.defaultDialogTitle(text).apply(config)
    }

    override fun title(textRes: Int, config: Text.() -> Unit) {
        title(context.getText(textRes), config)
    }

    override fun decorateListTop(config: ListDialogInterface.(ConstraintLayout) -> Unit) {
        listTopAreaConfig = config
    }

    override fun decorateListBottom(config: ListDialogInterface.(ConstraintLayout) -> Unit) {
        listBottomAreaConfig = config
    }

    override fun list(config: DisplayList.() -> Unit) {
        displayList = context.defaultDisplayList().apply(config)
    }

    override fun items(list: List<DisplayItem>, onItemClickListener: OnItemClickListener?) {
        displayList = context.defaultDisplayList().apply {
            items(list)
            onItemClickListener?.let { onItemClick(it) }
        }
    }

    override fun customizeList(config: ListDialogInterface.(RecyclerView) -> Unit) {
        customizeList = config
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

    override fun behavior(config: DialogBehavior.() -> Unit) {
        behavior = behavior.apply(config)
    }

    override fun toDescription(): ListDialogDescription {
        return ListDialogDescription(
            title = title?.toTextDescription(),
            listDescription = displayList?.toDisplayListDescription(),
            positiveButton = positiveButton?.toButtonDescription(),
            negativeButton = negativeButton?.toButtonDescription(),
            size = size.toDialogWindowSizeDescription(),
            behavior = behavior.toDialogBehaviorDescription(),
            listTopAreaConfig = listTopAreaConfig,
            listBottomAreaConfig = listBottomAreaConfig,
            customizeList = customizeList
        )
    }

}