package me.ztiany.wan.sample.presentation.mvi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.fragment.list.epoxy.BaseEpoxyListFragment
import com.android.base.fragment.list.handleListStateWithViewLifecycle
import com.android.base.fragment.ui.ListLayoutHost
import com.android.base.ui.recyclerview.MarginDecoration
import com.qmuiteam.qmui.kotlin.dip
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.ztiany.wan.sample.databinding.SampleFragmentFeedBinding
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO

class MVIListFragment : BaseEpoxyListFragment<ArticleVO, SampleFragmentFeedBinding>() {

    private val listController by lazy { MVIListController() }

    private val viewModel by viewModels<MVIViewModel>()

    private val loadMoreFlow = MutableSharedFlow<ArticleIntent.More>()

    private val reportFlow = MutableSharedFlow<ArticleIntent.Report>()

    private val intents by lazy {
        merge(
            flowOf(ArticleIntent.Init(1)),
            loadMoreFlow,
            reportFlow
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intents.onEach(viewModel::send).launchIn(lifecycleScope)
    }

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

    private fun subscribeViewModel() {
        handleListStateWithViewLifecycle(data = viewModel.articleState)
    }

    override fun onRefresh() {
        //viewModel.refresh()
    }

    override fun onLoadMore() {
        lifecycleScope.launch {
            loadMoreFlow.tryEmit(ArticleIntent.More(20))
        }
    }

}