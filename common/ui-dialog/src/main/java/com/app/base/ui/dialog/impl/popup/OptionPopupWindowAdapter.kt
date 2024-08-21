package com.app.base.ui.dialog.impl.popup

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.android.base.adapter.recycler.ViewBindingViewHolder
import com.android.base.adapter.recycler.segment.SimpleRecyclerAdapter
import com.android.base.utils.android.views.dip
import com.android.base.utils.android.views.getStyledDrawable
import com.android.base.utils.android.views.newMWLayoutParams
import com.android.base.utils.android.views.newOnItemClickListener
import com.android.base.viewbinding.DynamicViewBinding
import com.app.base.ui.dialog.dsl.Option
import com.app.base.ui.dialog.dsl.TextStyleDescription
import com.app.base.ui.dialog.dsl.applyTo
import com.google.android.material.textview.MaterialTextView


internal class OptionPopupWindowAdapter(
    context: Context,
    private val itemStyle: TextStyleDescription,
    items: List<Option>,
    onItemClicked: (index: Int, item: Option) -> Unit,
) : SimpleRecyclerAdapter<Option, DynamicViewBinding<TextView>>(context, items) {

    private val itemClickListener = newOnItemClickListener<Option> { _, item ->
        onItemClicked(items.indexOf(item), item)
    }

    override fun provideViewBinding(parent: ViewGroup, inflater: LayoutInflater) = DynamicViewBinding(
        MaterialTextView(parent.context).apply {
            setPadding(dip(20), dip(10), dip(20), dip(10))
            compoundDrawablePadding = dip(8)
            itemStyle.applyTo(this)
            background = context.getStyledDrawable(android.R.attr.selectableItemBackground)
            layoutParams = newMWLayoutParams()
        }
    )

    override fun onBindItem(viewHolder: ViewBindingViewHolder<DynamicViewBinding<TextView>>, item: Option) {
        viewHolder.vb.root.text = item.title
        if (item.icon != 0) {
            viewHolder.vb.root.setCompoundDrawablesWithIntrinsicBounds(item.icon, 0, 0, 0)
        } else {
            viewHolder.vb.root.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
        viewHolder.vb.root.tag = item
        viewHolder.vb.root.setOnClickListener(itemClickListener)
    }

}