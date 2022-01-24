package com.vclusters.cloud.account.presentation.switchover

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.android.base.adapter.recycler.BindingViewHolder
import com.android.base.adapter.recycler.RecyclerAdapter
import com.android.base.utils.android.views.getString
import com.android.base.utils.android.views.invisible
import com.android.base.utils.android.views.visibleOrInvisible
import com.app.base.services.usermanager.UserManager
import com.app.base.utils.hidePhoneNumber
import com.vclusters.cloud.account.R
import com.vclusters.cloud.account.data.HistoryUser
import com.vclusters.cloud.account.databinding.AccountLayoutItemSwitchBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

private const val ITEM_NORMAL = 1
private const val ITEM_ADD = 2

class SwitchAccountAdapter @Inject constructor(
    @ActivityContext context: Context,
    private val userManager: UserManager
) : RecyclerAdapter<HistoryUser, BindingViewHolder<AccountLayoutItemSwitchBinding>>(context) {

    /** view 的 tag 为 [HistoryUser]*/
    var onItemSelectedListener: View.OnClickListener? = null

    /** view 的 tag 为 [HistoryUser]*/
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
            viewHolder.vb.accountTvPhone.text = getString(R.string.add_account)
            viewHolder.itemView.setOnClickListener(onAddAccountClickListener)
            viewHolder.vb.accountTvSelected.invisible()
            viewHolder.vb.accountIvAvatar.setImageResource(R.drawable.img_add)
        } else {
            getItem(position)?.let {
                bindNormalItem(viewHolder, it)
            }
        }
    }

    private fun bindNormalItem(viewHolder: BindingViewHolder<AccountLayoutItemSwitchBinding>, item: HistoryUser) {
        val isSelected = userManager.user.phone == item.phone

        viewHolder.vb.accountTvPhone.text = hidePhoneNumber(item.phone)

        viewHolder.itemView.tag = item
        viewHolder.itemView.setOnClickListener(if (isSelected) null else onItemSelectedListener)
        viewHolder.itemView.isClickable = !isSelected

        viewHolder.vb.accountTvSelected.visibleOrInvisible(isSelected)
        viewHolder.vb.accountIvAvatar.setImageResource(R.drawable.img_default_avatar)
    }

}