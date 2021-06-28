package com.app.base.widget.dialog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.base.adapter.recycler.BindingViewHolder
import com.android.base.adapter.recycler.RecyclerAdapter
import com.android.base.app.ui.viewBinding
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.visible
import com.android.base.utils.android.views.visibleOrInvisible
import com.app.base.R
import com.app.base.databinding.DialogBottomSheetBinding
import com.app.base.databinding.DialogBottomSheetItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-03-14 17:01
 */
class AppBottomSheetDialog(
    private val builder: BottomSheetDialogBuilder
) : BottomSheetDialog(builder.context) {

    private val vb by viewBinding(DialogBottomSheetBinding::bind)

    init {
        setContentView(R.layout.dialog_bottom_sheet)
        setupList()
        setupBottomAction()
        setupTitle()
    }

    private fun setupTitle() {
        if (builder.titleText.isNotEmpty()) {
            vb.dialogBottomSheetTvTitle.visible()
            vb.dialogBottomSheetTvTitle.text = builder.titleText
        }
    }

    private fun setupBottomAction() {
        if (builder.actionText.isNotEmpty()) {
            vb.dialogBottomSheetTvBottomAction.text = builder.actionText
            vb.dialogBottomSheetVDivider.visible()
        } else {
            vb.dialogBottomSheetTvBottomAction.gone()
            vb.dialogBottomSheetVDivider.gone()
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
        super.show()
        //https://stackoverflow.com/questions/37104960/bottomsheetdialog-with-transparent-background
        findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.setBackgroundResource(
            android.R.color.transparent
        )
    }

}

private class BottomSheetDialogAdapter(
    context: Context,
    items: List<CharSequence>,
    private val builder: BottomSheetDialogBuilder,
    onItemClickedListener: (Int, CharSequence) -> Unit,
) : RecyclerAdapter<CharSequence, BindingViewHolder<DialogBottomSheetItemBinding>>(context, items) {

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
        viewType: Int
    ): BindingViewHolder<DialogBottomSheetItemBinding> {
        return BindingViewHolder(
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
        viewHolder: BindingViewHolder<DialogBottomSheetItemBinding>,
        position: Int
    ) {
        val item = getItem(position)
        viewHolder.vb.dialogBottomSheetTvItem.text = item
        viewHolder.itemView.tag = item
        viewHolder.itemView.setOnClickListener(onItemClickedListener)
        viewHolder.vb.dialogBottomSheetTvItemSelection.visibleOrInvisible(position == builder.selectedPosition)
    }

}