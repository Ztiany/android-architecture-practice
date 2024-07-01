package me.ztiany.wan.sample.presentation.paging3

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.fragment.list.paging3.BasePagingFragment
import com.android.base.fragment.list.paging3.handlePagingData
import com.android.base.fragment.list.paging3.withDefaultLoadStateFooter
import com.android.base.fragment.tool.runRepeatedlyOnViewLifecycle
import com.android.base.ui.recyclerview.MarginDecoration
import com.android.base.utils.common.unsafeLazy
import com.qmuiteam.qmui.kotlin.dip
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.sample.databinding.SampleFragmentFeedBinding

@AndroidEntryPoint
class SquareFragment : BasePagingFragment<SampleFragmentFeedBinding>() {

    private val viewModel by viewModels<SquareViewModel>()

    private val squareAdapter by unsafeLazy {
        SquareAdapter(requireContext())
    }

    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) = withVB {
        with(vb.mainRvArticles) {
            addItemDecoration(MarginDecoration(top = dip(10)))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = squareAdapter.withDefaultLoadStateFooter()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeViewModel()
    }

    override fun onRefresh() {
        squareAdapter.refresh()
    }

    private fun subscribeViewModel() = runRepeatedlyOnViewLifecycle {
        pagingLayoutController.handlePagingData(
            adapter = squareAdapter,
            data = viewModel.squareFlow
        )
    }

}