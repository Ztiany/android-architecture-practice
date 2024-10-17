package com.app.sample.view.paging3.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.android.base.adapter.recycler.ViewBindingViewHolder
import com.android.base.fragment.list.paging3.IntIdentityItemDiffCallback
import com.app.sample.view.databinding.SampleItemArticleBinding
import com.app.sample.view.epoxy.ArticleVO


internal class SquareAdapter(
    private val context: Context,
) : PagingDataAdapter<ArticleVO, ViewBindingViewHolder<SampleItemArticleBinding>>(IntIdentityItemDiffCallback()) {

    override fun onBindViewHolder(holder: ViewBindingViewHolder<SampleItemArticleBinding>, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.vb.mainTvAuthor.text = item.author
            holder.vb.mainTvTitle.text = item.title
            holder.vb.mainTvCategory.text = item.category
            holder.vb.mainTvTime.text = item.updateTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingViewHolder<SampleItemArticleBinding> {
        return ViewBindingViewHolder(SampleItemArticleBinding.inflate(LayoutInflater.from(context), parent, false))
    }

}