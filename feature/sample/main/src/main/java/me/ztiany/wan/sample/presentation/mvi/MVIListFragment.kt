package me.ztiany.wan.sample.presentation.mvi

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
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import timber.log.Timber

@AndroidEntryPoint
class MVIListFragment : BaseEpoxyListFragment<ArticleVO, SampleFragmentFeedBinding>() {

    private val listController by lazy { MVIListController() }

    private val viewModel by viewModels<MVIViewModel>()

    override fun provideListImplementation(view: View, savedInstanceState: Bundle?): ListLayoutHost<ArticleVO> {
        with(vb.mainRvArticles) {
            addItemDecoration(MarginDecoration(top = dip(10)))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listController.adapter
        }
        return setUpList(listController, listController.setUpLoadMore(vb.mainRvArticles))
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        subscribeViewModel()
    }

    private fun subscribeViewModel() = runRepeatedlyOnViewLifecycle {
        listLayoutController.handleListState(viewModel.articleState)
    }

    override fun onRefresh() {
        viewModel.send(ArticleIntent.Init)
    }

    override fun onLoadMore() {
        Timber.d("onLoadMore")
        viewModel.send(ArticleIntent.More)
    }

}