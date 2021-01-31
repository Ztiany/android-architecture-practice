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
import com.android.base.utils.android.views.visibleOrInvisible
import com.app.base.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_bottom_sheet.*
import kotlinx.android.synthetic.main.dialog_bottom_sheet_item.*

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-03-14 17:01
 */
class AppBottomSheetDialog(
        private val builder: BottomSheetDialogBuilder
) : BottomSheetDialog(builder.context) {

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
            dialogBottomSheetVDivider.visible()
        } else {
            dialogBottomSheetTvBottomAction.gone()
            dialogBottomSheetVDivider.gone()
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
                handleItemSelectedNormalMode(position, item)
            }
        }
    }

    private fun handleItemSelectedNormalMode(position: Int, item: CharSequence) {
        if (builder.autoDismiss) {
            dismiss()
        }
        builder.itemSelectedListener?.invoke(position, item)
        builder.itemSelectedListener2?.invoke(this, position, item)
    }

    override fun show() {
        super.show()
        //https://stackoverflow.com/questions/37104960/bottomsheetdialog-with-transparent-background
        findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.setBackgroundResource(android.R.color.transparent)
    }

}

private class BottomSheetDialogAdapter(
        context: Context,
        items: List<CharSequence>,
        private val builder: BottomSheetDialogBuilder,
        onItemClickedListener: (Int, CharSequence) -> Unit,
) : RecyclerAdapter<CharSequence, KtViewHolder>(context, items) {

    private lateinit var recyclerView: RecyclerView

    private val onItemClickedListener = View.OnClickListener {
        onItemClickedListener(recyclerView.getChildAdapterPosition(it), it.tag as CharSequence)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KtViewHolder {
        val containerView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_item, parent, false)
        return KtViewHolder(containerView).apply {
            dialogBottomSheetTvItem.gravity = builder.itemGravity
            dialogBottomSheetTvItem.setTextColor(builder.itemTextColor)
        }
    }

    override fun onBindViewHolder(viewHolder: KtViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.dialogBottomSheetTvItem.text = item
        viewHolder.itemView.tag = item
        viewHolder.itemView.setOnClickListener(onItemClickedListener)
        viewHolder.dialogBottomSheetTvItemSelection.visibleOrInvisible(position == builder.selectedPosition)
    }

}