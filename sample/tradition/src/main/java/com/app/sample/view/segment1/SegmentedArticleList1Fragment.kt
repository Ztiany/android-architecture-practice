package com.app.sample.view.segment1

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.fragment.list.handleListData
import com.android.base.fragment.list.handleListError
import com.android.base.fragment.list.handleListStartLoadMore
import com.android.base.fragment.list.handleListStartRefresh
import com.android.base.fragment.list.segment.CommonBaseListFragment
import com.android.base.fragment.list.segment.startListJob
import com.android.base.fragment.ui.SegmentedListLayoutHost
import com.android.base.fragment.ui.toSegmentedListDataHost
import com.android.base.ui.recyclerview.MarginDecoration
import com.android.base.utils.android.views.dip
import com.android.base.utils.common.unsafeLazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ensureActive
import com.app.sample.view.common.data.SampleRepository
import com.app.sample.view.databinding.SampleFragmentFeedBinding
import com.app.sample.view.epoxy.ArticleMapper
import com.app.sample.view.epoxy.ArticleVO
import javax.inject.Inject

@AndroidEntryPoint
class SegmentedArticleList1Fragment : CommonBaseListFragment<ArticleVO, SampleFragmentFeedBinding>() {

    @Inject internal lateinit var repository: SampleRepository

    @Inject internal lateinit var articleMapper: ArticleMapper

    private val articleAdapter by unsafeLazy { ArticleListAdapter(this) { deleteItem(it) } }

    override fun provideListImplementation(view: View, savedInstanceState: Bundle?): SegmentedListLayoutHost<ArticleVO, Int> {
        with(vb.mainRvArticles) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(MarginDecoration(vertical = dip(10)))
            // 如果需要开启加载更多：
            adapter = enableLoadMore(articleAdapter)
            // 如果不需要开启加载更多：
            //adapter = articleAdapter
        }
        return setUpList(articleAdapter.toSegmentedListDataHost())
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        listController.autoRefresh()
    }

    private fun deleteItem(article: ArticleVO) {
        articleAdapter.remove(article)
    }

    override fun onRefresh() {
        listController.handleListStartRefresh()
        startLoad(paging.size, paging.start)
    }

    override fun onLoadMore() {
        listController.handleListStartLoadMore()
        startLoad(paging.size, paging.next)
    }

    private fun startLoad(size: Int, page: Int) = startListJob {
        try {
            // 加载数据
            val articles = articleMapper.mapArticles(repository.loadHomeArticles(page, size))
            // 将加载到的数据交给 handleListData 处理
            listController.handleListData(articles, hasMore = { it.isNotEmpty() })
        } catch (error: Throwable) {
            ensureActive()
            // 发生错误，交给 handleListError 处理
            listController.handleListError(error)
        }
    }

}