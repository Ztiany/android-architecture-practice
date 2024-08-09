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
import com.app.base.ui.dialog.databinding.DialogItemBottomsheetSimpleBinding
import com.app.base.ui.dialog.dsl.DisplayListDescription
import com.app.base.ui.dialog.dsl.DisplayItem
import com.app.base.ui.dialog.dsl.applyTo

internal class ListDialogAdapter(
    context: Context,
    private val listDescription: DisplayListDescription,
    onItemClickedListener: (Int, DisplayItem) -> Unit,
) : SimpleRecyclerAdapter<DisplayItem, DialogItemBottomsheetSimpleBinding>(context, listDescription.items) {

    private lateinit var recyclerView: RecyclerView

    private val hasSubtitle = listDescription.items.any { it.subtitle.isNotEmpty() }

    private val onItemClickedListener = View.OnClickListener {
        val clickedItem = it.tag as DisplayItem
        onItemClickedListener(recyclerView.getChildAdapterPosition(it), clickedItem)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onViewHolderCreated(viewHolder: ViewBindingViewHolder<DialogItemBottomsheetSimpleBinding>) = with(viewHolder.vb) {
        listDescription.titleStyle.applyTo(dialogTvTitle)
        listDescription.titleStyle.applyTo(dialogTvSingleTitle)
        listDescription.subtitleStyle.applyTo(dialogTvSubtitle)
    }

    override fun onBindItem(
        viewHolder: ViewBindingViewHolder<DialogItemBottomsheetSimpleBinding>,
        item: DisplayItem,
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
        root.tag = item
        root.setOnClickListener(onItemClickedListener)
    }

}