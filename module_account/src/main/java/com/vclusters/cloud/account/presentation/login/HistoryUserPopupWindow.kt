package com.vclusters.cloud.account.presentation.login

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.adapter.recycler.BindingViewHolder
import com.android.base.adapter.recycler.SimpleRecyclerAdapter
import com.android.base.utils.android.adaption.OnItemClickListener
import com.android.base.utils.android.views.dip
import com.android.base.utils.android.views.measureSelfWithScreenSize
import com.vclusters.cloud.account.R
import com.vclusters.cloud.account.data.HistoryUser
import com.vclusters.cloud.account.databinding.AccountLayoutHistoryUserBinding
import com.vclusters.cloud.account.databinding.AccountLayoutItemHistoryUserBinding
import kotlin.math.min

private var itemHeight = 0
private const val MAX_ITEM_COUNT_TO_SHOW = 4

private fun calculateWindowHeight(itemHeight: Int, listCount: Int): Int {
    return itemHeight * min(MAX_ITEM_COUNT_TO_SHOW, listCount) + dip(20)/*RecyclerView's margin.*/
}

private fun getItemHeight(context: Context, parent: ViewGroup): Int {
    if (itemHeight == 0) {
        val itemView = LayoutInflater.from(context).inflate(R.layout.account_layout_item_history_user, parent, false)
        itemView.measureSelfWithScreenSize()
        itemHeight = itemView.measuredHeight
    }
    return itemHeight
}

fun showHistoryUserPopupWindow(
    context: Context,
    anchor: View,
    historyUsers: List<HistoryUser>,
    onSelected: (HistoryUser) -> Unit,
    onDeleted: (HistoryUser) -> Unit,
    onDismiss: () -> Unit
) {

    //popupWindow
    val popupWindow = PopupWindow(anchor.context)
    val binding = AccountLayoutHistoryUserBinding.inflate(LayoutInflater.from(context), null, false)
    popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    popupWindow.isOutsideTouchable = true
    popupWindow.contentView = binding.root
    popupWindow.setOnDismissListener { onDismiss() }

    //rv
    binding.accountRvHistoryUser.layoutManager = LinearLayoutManager(context)
    binding.accountRvHistoryUser.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    val historyUserAdapter = HistoryUserAdapter(context, historyUsers)
    historyUserAdapter.onItemSelected = onSelected
    historyUserAdapter.onDeleteItem = {
        if (historyUserAdapter.isEmpty()) {
            popupWindow.dismiss()
        }
        onDeleted(it)
        if (historyUserAdapter.itemCount < MAX_ITEM_COUNT_TO_SHOW) {
            val height = calculateWindowHeight(getItemHeight(context, binding.accountRvHistoryUser), historyUsers.size)
            popupWindow.update(anchor, 0, -(height + anchor.height), anchor.width, height)
        }
    }
    binding.accountRvHistoryUser.adapter = historyUserAdapter

    //show
    popupWindow.width = anchor.width
    popupWindow.height = calculateWindowHeight(getItemHeight(context, binding.accountRvHistoryUser), historyUsers.size)
    popupWindow.showAsDropDown(anchor, 0, -(popupWindow.height + anchor.height))
}

private class HistoryUserAdapter(
    context: Context,
    historyUsers: List<HistoryUser>
) : SimpleRecyclerAdapter<HistoryUser, AccountLayoutItemHistoryUserBinding>(context, historyUsers) {

    var onItemSelected: ((HistoryUser) -> Unit)? = null
    var onDeleteItem: ((HistoryUser) -> Unit)? = null

    private val _onItemSelected = object : OnItemClickListener<HistoryUser>() {
        override fun onClick(view: View, item: HistoryUser) {
            onItemSelected?.invoke(item)
        }
    }

    private val _onDeleteClicked = object : OnItemClickListener<HistoryUser>() {
        override fun onClick(view: View, item: HistoryUser) {
            remove(item)
            onDeleteItem?.invoke(item)
        }
    }

    override fun provideViewBinding(parent: ViewGroup, inflater: LayoutInflater): AccountLayoutItemHistoryUserBinding {
        return AccountLayoutItemHistoryUserBinding.inflate(inflater, parent, false)
    }

    override fun bindItem(viewHolder: BindingViewHolder<AccountLayoutItemHistoryUserBinding>, item: HistoryUser) {
        viewHolder.vb.accountTvPhone.text = item.phone
        viewHolder.vb.accountIvClear.tag = item
        viewHolder.vb.accountIvClear.setOnClickListener(_onDeleteClicked)
        viewHolder.itemView.tag = item
        viewHolder.itemView.setOnClickListener(_onItemSelected)
    }

}