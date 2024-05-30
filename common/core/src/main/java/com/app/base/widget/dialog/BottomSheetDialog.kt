package com.app.base.widget.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.base.adapter.recycler.ViewBindingViewHolder
import com.android.base.adapter.recycler.segment.BaseRecyclerAdapter
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.beVisible
import com.android.base.utils.android.views.beVisibleOrInvisible
import com.app.base.databinding.DialogBottomSheetBinding
import com.app.base.databinding.DialogBottomSheetItemBinding

/**
 *@author Ztiany
 */
class BottomSheetDialog(
    private val builder: BottomSheetDialogBuilder,
) : com.google.android.material.bottomsheet.BottomSheetDialog(builder.context) {

    private val vb = DialogBottomSheetBinding.inflate(LayoutInflater.from(builder.context))

    init {
        setContentView(vb.root)
        setupList()
        setupBottomAction()
        setupTitle()
    }

    private fun setupTitle() {
        if (builder.titleText.isNotEmpty()) {
            vb.dialogBottomSheetTvTitle.beVisible()
            vb.dialogBottomSheetTvTitle.text = builder.titleText
        }
    }

    private fun setupBottomAction() {
        if (builder.actionText.isNotEmpty()) {
            vb.dialogBottomSheetTvBottomAction.text = builder.actionText
            vb.dialogBottomSheetVDivider.beVisible()
        } else {
            vb.dialogBottomSheetTvBottomAction.beGone()
            vb.dialogBottomSheetVDivider.beGone()
        }

        vb.dialogBottomSheetTvBottomAction.setOnClickListener {
            dismiss()
            builder.actionListener?.invoke()
        }
    }

    private fun setupList() {
        val customList = builder.customList
        if (customList != null) {
            customList(this, vb.dialogBottomRvList)
        } else {
            defaultList()
        }
    }

    private fun defaultList() {
        vb.dialogBottomRvList.layoutManager = LinearLayoutManager(context)
        val items = builder.items
        if (items != null) {
            vb.dialogBottomRvList.adapter = BottomSheetDialogAdapter(
                context,
                items,
                builder
            ) { position: Int, item: CharSequence ->
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
        showCompat {
            super.show()
        }
        //https://stackoverflow.com/questions/37104960/bottomsheetdialog-with-transparent-background
        findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.apply {
            post {
                background = android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT)
            }
        }
    }

}

private class BottomSheetDialogAdapter(
    context: Context,
    items: List<CharSequence>,
    private val builder: BottomSheetDialogBuilder,
    onItemClickedListener: (Int, CharSequence) -> Unit,
) : BaseRecyclerAdapter<CharSequence, ViewBindingViewHolder<DialogBottomSheetItemBinding>>(context, items) {

    private lateinit var recyclerView: RecyclerView

    private val onItemClickedListener = View.OnClickListener {
        onItemClickedListener(recyclerView.getChildAdapterPosition(it), it.tag as CharSequence)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewBindingViewHolder<DialogBottomSheetItemBinding> {
        return ViewBindingViewHolder(
            DialogBottomSheetItemBinding.inflate(
                inflater,
                parent,
                false
            )
        ).apply {
            vb.dialogBottomSheetTvItem.gravity = builder.itemGravity
            vb.dialogBottomSheetTvItem.setTextColor(builder.itemTextColor)
        }
    }

    override fun onBindViewHolder(
        viewHolder: ViewBindingViewHolder<DialogBottomSheetItemBinding>,
        position: Int,
    ) {
        val item = getItem(position)
        viewHolder.vb.dialogBottomSheetTvItem.text = item
        viewHolder.itemView.tag = item
        viewHolder.itemView.setOnClickListener(onItemClickedListener)
        viewHolder.vb.dialogBottomSheetTvItemSelection.beVisibleOrInvisible(position == builder.selectedPosition)
    }

}