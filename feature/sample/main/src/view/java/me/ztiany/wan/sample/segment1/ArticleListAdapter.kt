package me.ztiany.wan.sample.segment1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.base.adapter.recycler.ViewBindingViewHolder
import com.android.base.adapter.recycler.segment.SimpleRecyclerAdapter
import com.android.base.utils.android.views.newOnItemClickListener
import me.ztiany.wan.sample.databinding.SampleItemArticleBinding
import me.ztiany.wan.sample.epoxy.ArticleVO

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