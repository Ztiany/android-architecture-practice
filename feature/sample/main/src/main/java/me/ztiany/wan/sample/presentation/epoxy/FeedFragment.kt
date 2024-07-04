package me.ztiany.wan.sample.presentation.epoxy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.fragment.list.epoxy.BaseEpoxyListFragment
import com.android.base.fragment.list.handleListState
import com.android.base.fragment.tool.runRepeatedlyOnViewLifecycle
import com.android.base.fragment.ui.ListLayoutHost
import com.android.base.ui.recyclerview.MarginDecoration
import com.qmuiteam.qmui.kotlin.dip
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.sample.databinding.SampleFragmentFeedBinding

@AndroidEntryPoint
class FeedFragment : BaseEpoxyListFragment<FeedItem, SampleFragmentFeedBinding>() {

    private val listController by lazy { FeedListController() }

    private val viewModel by viewModels<FeedViewModule>()

    override fun provideListImplementation(view: View, savedInstanceState: Bundle?): ListLayoutHost<FeedItem> {
        with(vb.mainRvArticles) {
            addItemDecoration(MarginDecoration(top = dip(10)))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listController.adapter
        }
        return setUpList(listController, listController.setUpLoadMore(vb.mainRvArticles))
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        invokeOnEnterTransitionEnd { subscribeViewModel() }
    }

    private fun subscribeViewModel() = runRepeatedlyOnViewLifecycle {
        listLayoutController.handleListState(viewModel.articles)
    }

    override fun onRefresh() {
        viewModel.refresh()
    }

    override fun onLoadMore() {
        viewModel.loadMore()
    }

}