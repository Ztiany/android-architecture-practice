package me.ztiany.wan.sample.presentation.segment1

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.fragment.list.handleListData
import com.android.base.fragment.list.handleListError
import com.android.base.fragment.list.handleListStartLoadMore
import com.android.base.fragment.list.handleListStartRefresh
import com.android.base.fragment.list.segment.BaseListFragment
import com.android.base.fragment.list.segment.startListJob
import com.android.base.fragment.ui.ListLayoutHost
import com.android.base.fragment.ui.toListDataHost
import com.android.base.ui.recyclerview.MarginDecoration
import com.android.base.utils.android.views.dip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ensureActive
import me.ztiany.wan.sample.data.SampleRepository
import me.ztiany.wan.sample.databinding.SampleFragmentFeedBinding
import me.ztiany.wan.sample.presentation.epoxy.ArticleMapper
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SegmentedArticleList1Fragment : BaseListFragment<ArticleVO, SampleFragmentFeedBinding>() {

    @Inject internal lateinit var repository: SampleRepository

    @Inject internal lateinit var articleMapper: ArticleMapper

    private val articleAdapter by lazy {
        ArticleListAdapter(this) { deleteItem(it) }
    }

    private val articleViewModel by viewModels<ArticleViewModel>()

    override fun provideListImplementation(view: View, savedInstanceState: Bundle?): ListLayoutHost<ArticleVO> {
        with(vb.mainRvArticles) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(MarginDecoration(vertical = dip(10)))
            // 如果需要开启加载更多：
            adapter = enableLoadMore(articleAdapter)
            // 如果不需要开启加载更多：
            //adapter = articleAdapter
        }
        return setUpList(articleAdapter.toListDataHost())
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        autoRefresh()
    }

    private fun deleteItem(article: ArticleVO) {
        Timber.d("list size = ${articleAdapter.getDataSize()}")
        articleAdapter.remove(article)
    }

    override fun onRefresh() {
        handleListStartRefresh()
        startLoad(paging.size, paging.start)
    }

    override fun onLoadMore() {
        handleListStartLoadMore()
        startLoad(paging.size, paging.next)
    }

    private fun startLoad(size: Int, page: Int) = startListJob {
        try {
            // 加载数据
            val articles = articleMapper.mapArticles(repository.loadHomeArticles(page, size))
            // 将加载到的数据交给 handleListData 处理
            handleListData(articles, hasMore = { it.isNotEmpty() })
        } catch (error: Throwable) {
            ensureActive()
            // 发生错误，交给 handleListError 处理
            handleListError(error)
        }
    }

}