package me.ztiany.wan.sample.presentation.segment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.fragment.list.handleListData
import com.android.base.fragment.list.handleListError
import com.android.base.fragment.list.handleListLoading
import com.android.base.fragment.list.segment.BaseListFragment
import com.android.base.fragment.ui.ListLayoutHost
import com.android.base.fragment.ui.toListDataHost
import com.android.base.ui.recyclerview.MarginDecoration
import com.android.base.utils.android.views.dip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import me.ztiany.wan.sample.databinding.SampleFragmentFeedBinding
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import timber.log.Timber

@AndroidEntryPoint
class ArticleListFragment : BaseListFragment<ArticleVO, SampleFragmentFeedBinding>() {

    private val articleAdapter by lazy {
        ArticleListAdapter(this) {
            deleteItem(it)
        }
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
        // 处理 loading
        handleListLoading()
        // 开始刷新
        startLoad(paging.size, paging.start)
    }

    override fun onLoadMore() {
        startLoad(paging.size, paging.next)
    }

    private fun startLoad(size: Int, page: Int) = lifecycleScope.launch {
        try {
            // 加载到的数据交给 handleListData 处理
            handleListData(articleViewModel.loadArticleList(page, size))
        } catch (error: Throwable) {
            ensureActive()
            // 发生错误，交给 handleListError 处理
            handleListError(error)
        }
    }

}