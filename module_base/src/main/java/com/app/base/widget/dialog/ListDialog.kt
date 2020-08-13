package com.app.base.widget.dialog

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.base.adapter.recycler.KtViewHolder
import com.android.base.adapter.recycler.SimpleRecyclerAdapter
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.visible
import com.app.base.R
import kotlinx.android.synthetic.main.dialog_list_item.*
import kotlinx.android.synthetic.main.dialog_list_layout.*

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

    private val dialogController = object : DialogController {
        override var positiveEnable: Boolean
            get() = dblListDialogBottom?.positiveEnable ?: false
            set(value) {
                dblListDialogBottom?.positiveEnable = value
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
            tvListDialogTitle.visible()
            tvListDialogTitle.text = title
            tvListDialogTitle.textSize = listDialogBuilder.titleSize
            tvListDialogTitle.setTextColor(listDialogBuilder.titleColor)
        } else {
            tvListDialogTitle.gone()
        }

        //cancel
        dblListDialogBottom.negativeText(listDialogBuilder.negativeText)
        dblListDialogBottom.onNegativeClick(View.OnClickListener {
            checkDismiss(listDialogBuilder)
            listDialogBuilder.negativeListener?.invoke()
        })

        //confirm
        dblListDialogBottom.positiveText(listDialogBuilder.positiveText)

        //list
        rvDialogListContent.layoutManager = LinearLayoutManager(context)
        val items = listDialogBuilder.items
        val adapter = listDialogBuilder.adapter

        if (items != null) {
            setupDefault(items, listDialogBuilder)
        } else if (adapter != null) {
            setupUsingSpecifiedAdapter(adapter, listDialogBuilder)
        }

        setCanceledOnTouchOutside(listDialogBuilder.cancelableTouchOutside)
        setCancelable(listDialogBuilder.cancelable)
    }

    private fun setupUsingSpecifiedAdapter(adapter: RecyclerView.Adapter<*>?, listDialogBuilder: ListDialogBuilder) {
        rvDialogListContent.adapter = adapter
        dblListDialogBottom.onPositiveClick(View.OnClickListener {
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

        rvDialogListContent.adapter = Adapter(context, items.toList())
        dblListDialogBottom.onPositiveClick(View.OnClickListener {
            checkDismiss(listDialogBuilder)
            listDialogBuilder.positiveListener?.invoke(selectedItemIndex, items[selectedItemIndex])
        })

        rvDialogListContent.post {
            rvDialogListContent?.smoothScrollToPosition(selectedItemIndex)
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

    private inner class Adapter internal constructor(
            context: Context,
            data: List<CharSequence>
    ) : SimpleRecyclerAdapter<CharSequence>(context, data) {

        private val onClickListener = View.OnClickListener { view ->
            setPosition(rvDialogListContent.getChildAdapterPosition(view))
        }

        private fun setPosition(childAdapterPosition: Int) {
            val oldSelectedItemIndex = selectedItemIndex
            selectedItemIndex = childAdapterPosition
            notifyItemChanged(oldSelectedItemIndex)
            notifyItemChanged(selectedItemIndex)
        }

        private val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setPosition(buttonView.tag as Int)
            }
        }

        private val alwaysCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, _ ->
            buttonView.isChecked = true
        }

        override fun provideLayout(parent: ViewGroup, viewType: Int) = R.layout.dialog_list_item

        override fun bind(viewHolder: KtViewHolder, item: CharSequence) {
            viewHolder.dialogListItemTv.text = item
            val isSelected = viewHolder.adapterPosition == selectedItemIndex
            viewHolder.dialogListItemCb.setOnCheckedChangeListener(null)
            viewHolder.dialogListItemCb.isChecked = isSelected

            if (isSelected) {
                viewHolder.dialogListItemCb.setOnCheckedChangeListener(alwaysCheckedChangeListener)
            } else {
                viewHolder.dialogListItemCb.tag = viewHolder.adapterPosition
                viewHolder.dialogListItemCb.setOnCheckedChangeListener(onCheckedChangeListener)
            }
            viewHolder.itemView.setOnClickListener(onClickListener)
        }

    }

}
