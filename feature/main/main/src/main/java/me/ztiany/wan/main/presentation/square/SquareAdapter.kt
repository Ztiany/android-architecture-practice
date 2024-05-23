package me.ztiany.wan.main.presentation.square

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.android.base.adapter.recycler.BindingViewHolder
import com.android.base.fragment.list.paging3.IntIdentityDiffCallback
import me.ztiany.wan.main.databinding.MainItemArticleBinding
import me.ztiany.wan.main.presentation.feed.ArticleVO


internal class SquareAdapter(
    private val context: Context,
) : PagingDataAdapter<ArticleVO, BindingViewHolder<MainItemArticleBinding>>(IntIdentityDiffCallback()) {

    override fun onBindViewHolder(holder: BindingViewHolder<MainItemArticleBinding>, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.vb.mainTvAuthor.text = item.author
            holder.vb.mainTvTitle.text = item.title
            holder.vb.mainTvCategory.text = item.category
            holder.vb.mainTvTime.text = item.updateTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<MainItemArticleBinding> {
        return BindingViewHolder(MainItemArticleBinding.inflate(LayoutInflater.from(context), parent, false))
    }

}