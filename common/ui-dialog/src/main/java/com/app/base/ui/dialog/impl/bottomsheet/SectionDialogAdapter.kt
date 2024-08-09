package com.app.base.ui.dialog.impl.bottomsheet

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.base.adapter.recycler.ViewBindingViewHolder
import com.android.base.adapter.recycler.segment.SimpleRecyclerAdapter
import com.android.base.utils.android.views.beInvisible
import com.android.base.utils.android.views.beVisible
import com.android.base.utils.android.views.setViewsGone
import com.android.base.utils.android.views.setViewsInvisible
import com.android.base.utils.android.views.setViewsVisible
import com.app.base.ui.dialog.databinding.DialogItemBottomsheetSelectionBinding
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.SelectionListDescription
import com.app.base.ui.dialog.dsl.applyTo

internal class SectionDialogAdapter(
    context: Context,
    private val listDescription: SelectionListDescription,
    onItemClickedListener: (Int, Selection) -> Unit,
) : SimpleRecyclerAdapter<Selection, DialogItemBottomsheetSelectionBinding>(context, listDescription.items) {

    private lateinit var recyclerView: RecyclerView

    private val hasSubtitle = listDescription.items.any { it.subtitle.isNotEmpty() }

    private val onItemClickedListener = View.OnClickListener {
        val clickedItem = it.tag as Selection
        onItemClickedListener(recyclerView.getChildAdapterPosition(it), clickedItem)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onViewHolderCreated(viewHolder: ViewBindingViewHolder<DialogItemBottomsheetSelectionBinding>) = with(viewHolder.vb) {
        listDescription.titleStyle.applyTo(dialogTvTitle)
        listDescription.titleStyle.applyTo(dialogTvSingleTitle)
        listDescription.subtitleStyle.applyTo(dialogTvSubtitle)
    }

    override fun onBindItem(
        viewHolder: ViewBindingViewHolder<DialogItemBottomsheetSelectionBinding>,
        item: Selection,
    ) = with(viewHolder.vb) {
        if (item.subtitle.isNotEmpty()) {
            dialogTvTitle.text = item.title
            dialogTvSubtitle.text = item.subtitle
            setViewsVisible(dialogTvSubtitle, dialogTvTitle)
            dialogTvSingleTitle.beInvisible()
        } else {
            if (hasSubtitle) {
                dialogTvSingleTitle.text = item.title
                dialogTvSingleTitle.beVisible()
                setViewsInvisible(dialogTvTitle, dialogTvSubtitle)
            } else {
                dialogTvTitle.text = item.title
                dialogTvTitle.beVisible()
                setViewsGone(dialogTvSingleTitle, dialogTvSubtitle)
            }
        }
        dialogIvCheckbox.isSelected = item.selected
        root.tag = item
        root.setOnClickListener(onItemClickedListener)
    }

    fun checkAll() {
        replaceAll(getList().map { it.copy(selected = true) })
    }

}