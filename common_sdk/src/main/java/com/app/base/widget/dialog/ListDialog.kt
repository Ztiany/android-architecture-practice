package com.app.base.widget.dialog

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.base.adapter.recycler.BindingViewHolder
import com.android.base.adapter.recycler.SimpleRecyclerAdapter
import com.android.base.app.ui.viewBinding
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.visible
import com.app.base.R
import com.app.base.databinding.DialogListItemBinding
import com.app.base.databinding.DialogListLayoutBinding

/**
 * 列表对话框
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-12-12 19:51
 */
internal class ListDialog(
    listDialogBuilder: ListDialogBuilder
) : BaseDialog(listDialogBuilder.context, true, listDialogBuilder.style) {

    private var selectedItemIndex: Int = 0
    private var selectableBgId = 0

    private val viewBinding by viewBinding(DialogListLayoutBinding::bind)

    private val dialogController = object : DialogController {
        override var positiveEnable: Boolean
            get() = viewBinding.dblListDialogBottom.positiveEnable ?: false
            set(value) {
                viewBinding.dblListDialogBottom.positiveEnable = value
            }
    }

    init {
        maxDialogWidthPercent = listDialogBuilder.maxWidthPercent

        setContentView(R.layout.dialog_list_layout)
        getSelectedBg()
        applyListDialogBuilder(listDialogBuilder)

        listDialogBuilder.onDialogPreparedListener?.invoke(this, dialogController)
    }

    private fun applyListDialogBuilder(listDialogBuilder: ListDialogBuilder) {
        //title
        val title = listDialogBuilder.title
        if (title != null) {
            viewBinding.tvListDialogTitle.visible()
            viewBinding.tvListDialogTitle.text = title
            viewBinding.tvListDialogTitle.textSize = listDialogBuilder.titleSize
            viewBinding.tvListDialogTitle.setTextColor(listDialogBuilder.titleColor)
        } else {
            viewBinding.tvListDialogTitle.gone()
        }

        //cancel
        viewBinding.dblListDialogBottom.negativeText(listDialogBuilder.negativeText)
        viewBinding.dblListDialogBottom.onNegativeClick(View.OnClickListener {
            checkDismiss(listDialogBuilder)
            listDialogBuilder.negativeListener?.invoke()
        })

        //confirm
        viewBinding.dblListDialogBottom.positiveText(listDialogBuilder.positiveText)

        //list
        viewBinding.rvDialogListContent.layoutManager = LinearLayoutManager(context)
        val items = listDialogBuilder.items
        val adapter = listDialogBuilder.adapter
        if (items != null) {
            setupDefault(items, listDialogBuilder)
        } else if (adapter != null) {
            setupUsingSpecifiedAdapter(adapter, listDialogBuilder)
        }

        //cancelable
        setCanceledOnTouchOutside(listDialogBuilder.cancelableTouchOutside)
        setCancelable(listDialogBuilder.cancelable)
    }

    private fun setupUsingSpecifiedAdapter(
        adapter: RecyclerView.Adapter<*>?,
        listDialogBuilder: ListDialogBuilder
    ) {
        viewBinding.rvDialogListContent.adapter = adapter
        viewBinding.dblListDialogBottom.onPositiveClick(View.OnClickListener {
            checkDismiss(listDialogBuilder)
            listDialogBuilder.positiveListener?.invoke(-1, "")
        })
    }

    private fun setupDefault(items: Array<CharSequence>, listDialogBuilder: ListDialogBuilder) {
        val size = items.size
        selectedItemIndex = listDialogBuilder.selectedPosition

        if (selectedItemIndex < 0) {
            selectedItemIndex = 0
        } else if (selectedItemIndex >= size) {
            selectedItemIndex = size - 1
        }

        viewBinding.rvDialogListContent.adapter = Adapter(context, items.toList())
        viewBinding.dblListDialogBottom.onPositiveClick(View.OnClickListener {
            checkDismiss(listDialogBuilder)
            listDialogBuilder.positiveListener?.invoke(selectedItemIndex, items[selectedItemIndex])
        })

        viewBinding.rvDialogListContent.post {
            viewBinding.rvDialogListContent.smoothScrollToPosition(selectedItemIndex)
        }
    }

    private fun checkDismiss(listDialogBuilder: ListDialogBuilder) {
        if (listDialogBuilder.autoDismiss) {
            dismiss()
        }
    }

    private fun getSelectedBg() {
        try {
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            selectableBgId = outValue.resourceId
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class Adapter(
        context: Context,
        data: List<CharSequence>
    ) : SimpleRecyclerAdapter<CharSequence, DialogListItemBinding>(context, data) {

        private val onClickListener = View.OnClickListener { view ->
            setPosition(viewBinding.rvDialogListContent.getChildAdapterPosition(view))
        }

        private fun setPosition(childAdapterPosition: Int) {
            val oldSelectedItemIndex = selectedItemIndex
            selectedItemIndex = childAdapterPosition
            notifyItemChanged(oldSelectedItemIndex)
            notifyItemChanged(selectedItemIndex)
        }

        private val onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    setPosition(buttonView.tag as Int)
                }
            }

        private val alwaysCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, _ ->
                buttonView.isChecked = true
            }

        override fun provideViewBinding(parent: ViewGroup, inflater: LayoutInflater) =
            DialogListItemBinding.inflate(inflater, parent, false)

        override fun bind(
            viewHolder: BindingViewHolder<DialogListItemBinding>,
            item: CharSequence
        ) {
            viewHolder.vb.dialogListItemTv.text = item
            val isSelected = viewHolder.adapterPosition == selectedItemIndex
            viewHolder.vb.dialogListItemCb.setOnCheckedChangeListener(null)
            viewHolder.vb.dialogListItemCb.isChecked = isSelected

            if (isSelected) {
                viewHolder.vb.dialogListItemCb.setOnCheckedChangeListener(
                    alwaysCheckedChangeListener
                )
            } else {
                viewHolder.vb.dialogListItemCb.tag = viewHolder.adapterPosition
                viewHolder.vb.dialogListItemCb.setOnCheckedChangeListener(onCheckedChangeListener)
            }
            viewHolder.itemView.setOnClickListener(onClickListener)
        }

    }

}