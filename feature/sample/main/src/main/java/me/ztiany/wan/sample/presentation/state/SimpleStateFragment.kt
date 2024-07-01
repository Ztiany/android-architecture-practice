package me.ztiany.wan.sample.presentation.state

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.fragment.state.BaseStateFragment
import com.android.base.fragment.state.handleFlowDataState
import com.android.base.fragment.tool.runRepeatedlyOnViewLifecycle
import com.android.base.ui.recyclerview.MarginDecoration
import com.android.base.utils.common.unsafeLazy
import com.qmuiteam.qmui.kotlin.dip
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.sample.databinding.SampleFragmentFeedBinding
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import me.ztiany.wan.sample.presentation.segment1.ArticleListAdapter

@AndroidEntryPoint
class SimpleStateFragment : BaseStateFragment<SampleFragmentFeedBinding>() {

    private val articleAdapter by unsafeLazy { ArticleListAdapter(this) { deleteItem(it) } }

    private val viewModel by viewModels<SimpleStateViewModel>()

    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) = withVB {
        with(mainRvArticles) {
            addItemDecoration(MarginDecoration(top = dip(10)))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) = runRepeatedlyOnViewLifecycle {
        stateLayoutController.handleFlowDataState(viewModel.articles) {
            onResult { list ->
                articleAdapter.replaceAll(list)
            }
        }
    }

    override fun onRefresh() {
        viewModel.refresh()
    }

    private fun deleteItem(item: ArticleVO) {
        // TODO: ask ViewModel to delete the item.
    }

}