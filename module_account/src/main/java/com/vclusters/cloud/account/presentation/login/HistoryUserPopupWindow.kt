package com.vclusters.cloud.account.presentation.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.android.base.utils.android.views.dip
import com.android.base.utils.android.views.measureSelfWithScreenSize
import com.vclusters.cloud.account.R
import com.vclusters.cloud.account.data.HistoryUser
import com.vclusters.cloud.account.databinding.AccountLayoutHistoryUserBinding
import timber.log.Timber

private var windowHeight = 0

fun showHistoryUserPopupWindow(context: Context, anchor: View, historyUsers: List<HistoryUser>) {

    val popupWindow = PopupWindow(anchor.context)
    popupWindow.width = anchor.width
    val binding = AccountLayoutHistoryUserBinding.inflate(LayoutInflater.from(context), null, false)
    popupWindow.contentView = binding.root

    if (windowHeight == 0) {
        val itemView = LayoutInflater.from(context).inflate(R.layout.account_layout_item_history_user, binding.root, false)
        itemView.measureSelfWithScreenSize()
        windowHeight = itemView.measuredHeight * historyUsers.size + dip(20)/*RecyclerView's margin.*/
    }

    popupWindow.height = windowHeight

    Timber.d("${anchor.width}, $windowHeight")

    popupWindow.showAsDropDown(anchor, 0, -(windowHeight + anchor.height))
}