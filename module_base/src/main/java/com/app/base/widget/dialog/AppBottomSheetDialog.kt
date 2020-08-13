package com.app.base.widget.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.base.adapter.recycler.KtViewHolder
import com.android.base.adapter.recycler.RecyclerAdapter
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.visible
import com.app.base.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_bottom_sheet.*
import kotlinx.android.synthetic.main.dialog_bottom_sheet_item.*


/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-03-14 17:01
 */
class AppBottomSheetDialog(private val builder: BottomSheetDialogBuilder) : BottomSheetDialog(builder.context) {

    init {
        setContentView(R.layout.dialog_bottom_sheet)
        setupList()
        setupBottomAction()
        setupTitle()
    }

    private fun setupTitle() {
        if (builder.titleText.isNotEmpty()) {
            dialogBottomSheetTvTitle.visible()
            dialogBottomSheetTvTitle.text = builder.titleText
        }
    }

    private fun setupBottomAction() {
        if (builder.actionText.isNotEmpty()) {
            dialogBottomSheetTvBottomAction.text = builder.actionText
        } else {
            dialogBottomSheetTvBottomAction.gone()
        }
        dialogBottomSheetTvBottomAction.setOnClickListener {
            dismiss()
            builder.actionListener?.invoke()
        }
    }

    private fun setupList() {
        val customList = builder.customList
        if (customList != null) {
            customList(this, dialogBottomRvList)
        } else {
            defaultList()
        }
    }

    private fun defaultList() {
        dialogBottomRvList.layoutManager = LinearLayoutManager(context)
        val items = builder.items
        if (items != null) {
            dialogBottomRvList.adapter = BottomSheetDialogAdapter(context, items, builder) { position: Int, item: CharSequence ->
                dismiss()
                builder.itemSelectedListener?.invoke(position, item)
            }
        }
    }

}

private class BottomSheetDialogAdapter(
        context: Context, items: List<CharSequence>,
        private val builder: BottomSheetDialogBuilder,
        onItemClickedListener: (Int, CharSequence) -> Unit
) : RecyclerAdapter<CharSequence, KtViewHolder>(context, items) {

    private lateinit var mRecyclerView: RecyclerView

    private val mOnItemClickedListener = View.OnClickListener {
        onItemClickedListener(mRecyclerView.getChildAdapterPosition(it), it.tag as CharSequence)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            KtViewHolder(LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_item, parent, false)).apply {
                dialogBottomSheetTvItem.gravity = builder.itemGravity
                dialogBottomSheetTvItem.setTextColor(builder.itemTextColor)
            }

    override fun onBindViewHolder(viewHolder: KtViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.dialogBottomSheetTvItem.text = item
        viewHolder.itemView.tag = item
        viewHolder.itemView.setOnClickListener(mOnItemClickedListener)
    }

}