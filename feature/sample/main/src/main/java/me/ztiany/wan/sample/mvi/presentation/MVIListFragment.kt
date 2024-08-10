package me.ztiany.wan.sample.mvi.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.fragment.list.epoxy.BaseEpoxyListFragment
import com.android.base.fragment.list.handleListState
import com.android.base.fragment.tool.runRepeatedlyOnViewLifecycle
import com.android.base.fragment.ui.ListLayoutHost
import com.android.base.ui.recyclerview.MarginDecoration
import com.qmuiteam.qmui.kotlin.dip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.ztiany.wan.sample.databinding.SampleFragmentFeedBinding
import me.ztiany.wan.sample.epoxy.ArticleVO
import timber.log.Timber

@AndroidEntryPoint
class MVIListFragment : BaseEpoxyListFragment<ArticleVO, SampleFragmentFeedBinding>() {

    private val listController by lazy { MVIListController() }

    private val viewModel by viewModels<MVIViewModel>()

    private val reportIntent = Channel<ArticleVO>(Channel.BUFFERED)
    private val loadingIntent = MutableSharedFlow<ArticleIntent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        merge(
            flowOf(ArticleIntent.Init),
            reportIntent.receiveAsFlow().map { ArticleIntent.Report(it.id) },
            loadingIntent
        ).onEach {
            Timber.d("send intent: $it")
            viewModel.send(it)
        }.launchIn(lifecycleScope)
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
        invokeOnEnterTransitionEnd {
            subscribeViewModel()
        }
    }

    private fun subscribeViewModel() = runRepeatedlyOnViewLifecycle {
        listLayoutController.handleListState(viewModel.articleState)

        viewModel.articleEvents.onEach {
            Timber.d("receive event: $it")
        }.launchIn(this)
    }

    override fun onRefresh() = lifecycleScope.launch {
        Timber.d("onRefresh")
        loadingIntent.emit(ArticleIntent.Init)
    }.drop()

    override fun onLoadMore() = lifecycleScope.launch {
        Timber.d("onLoadMore")
        loadingIntent.emit(ArticleIntent.More)
    }.drop()
}