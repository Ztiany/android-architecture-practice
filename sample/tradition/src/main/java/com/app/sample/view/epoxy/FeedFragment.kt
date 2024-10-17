package com.app.sample.view.epoxy

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.fragment.list.epoxy.BaseEpoxyListFragment
import com.android.base.fragment.list.handleListState
import com.android.base.fragment.tool.runRepeatedlyOnViewLifecycle
import com.android.base.fragment.ui.ListLayoutHost
import com.android.base.ui.recyclerview.MarginDecoration
import com.android.base.utils.android.views.dip
import dagger.hilt.android.AndroidEntryPoint
import com.app.sample.view.databinding.SampleFragmentFeedBinding

@AndroidEntryPoint
class FeedFragment : BaseEpoxyListFragment<FeedItem, SampleFragmentFeedBinding>() {

    private val epoxyListController by lazy { FeedListController() }

    private val viewModel by viewModels<FeedViewModule>()

    override fun provideListImplementation(view: View, savedInstanceState: Bundle?): ListLayoutHost<FeedItem> {
        with(vb.mainRvArticles) {
            addItemDecoration(MarginDecoration(top = dip(10)))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = epoxyListController.adapter
        }
        return setUpList(epoxyListController, epoxyListController.setUpLoadMore(vb.mainRvArticles))
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        invokeOnEnterTransitionEnd { subscribeViewModel() }
    }

    private fun subscribeViewModel() = runRepeatedlyOnViewLifecycle {
        listController.handleListState(viewModel.articles)
    }

    override fun onRefresh() {
        viewModel.refresh()
    }

    override fun onLoadMore() {
        viewModel.loadMore()
    }

}