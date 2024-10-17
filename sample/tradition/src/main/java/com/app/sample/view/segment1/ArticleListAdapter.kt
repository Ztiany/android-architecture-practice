package com.app.sample.view.segment1

import androidx.fragment.app.Fragment
import com.android.base.adapter.recycler.ViewBindingViewHolder
import com.android.base.adapter.recycler.segment.SimpleRecyclerAdapter
import com.android.base.utils.android.views.newOnItemClickListener
import com.app.sample.view.databinding.SampleItemArticleBinding
import com.app.sample.view.epoxy.ArticleVO

class ArticleListAdapter(
    fragment: Fragment,
    private val onItemClicked: (ArticleVO) -> Unit,
) : SimpleRecyclerAdapter<ArticleVO, SampleItemArticleBinding>(fragment.requireContext()) {

    private val onItemClickListener = newOnItemClickListener<ArticleVO> { _, item ->
        onItemClicked(item)
    }

    override fun onBindItem(viewHolder: ViewBindingViewHolder<SampleItemArticleBinding>, item: ArticleVO) = with(viewHolder) {
        vb.mainTvAuthor.text = item.author
        vb.mainTvTitle.text = item.title
        vb.mainTvCategory.text = item.category
        vb.mainTvTime.text = item.updateTime
        itemView.tag = item
        itemView.setOnClickListener(onItemClickListener)
    }

}