package com.vclusters.cloud.account.presentation.switch

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.android.base.adapter.recycler.BindingViewHolder
import com.android.base.adapter.recycler.RecyclerAdapter
import com.android.base.utils.android.views.visibleOrInvisible
import com.app.base.utils.hidePhoneNumber
import com.vclusters.cloud.account.data.HistoryUser
import com.vclusters.cloud.account.databinding.AccountLayoutItemSwitchBinding

private const val ITEM_NORMAL = 1
private const val ITEM_ADD = 2

class SwitchListAdapter(
    context: Context,
    list: List<HistoryUser>,
    private val currentUserPhone: String,
) : RecyclerAdapter<HistoryUser, BindingViewHolder<AccountLayoutItemSwitchBinding>>(context, list) {

    var onItemSelectedListener: View.OnClickListener? = null
    var onAddAccountClickListener: View.OnClickListener? = null

    override fun getDataSize(): Int {
        return super.getDataSize() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == getDataSize() - 1) {
            ITEM_ADD
        } else ITEM_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<AccountLayoutItemSwitchBinding> {
        return BindingViewHolder(AccountLayoutItemSwitchBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(viewHolder: BindingViewHolder<AccountLayoutItemSwitchBinding>, position: Int) {
        if (getItemViewType(position) == ITEM_ADD) {
            viewHolder.vb.accountTvPhone.text = "添加账号"
            viewHolder.itemView.setOnClickListener(onAddAccountClickListener)
        } else {
            getItem(position)?.let {
                bindNormalItem(viewHolder, it)
            }
        }
    }

    private fun bindNormalItem(viewHolder: BindingViewHolder<AccountLayoutItemSwitchBinding>, item: HistoryUser) {
        viewHolder.vb.accountTvPhone.text = hidePhoneNumber(item.phone)
        viewHolder.itemView.tag = item
        val isSelected = currentUserPhone == item.password
        viewHolder.vb.accountTvSelected.visibleOrInvisible(isSelected)
        viewHolder.itemView.setOnClickListener(if (isSelected) null else onItemSelectedListener)
    }

}