package me.ztiany.wan.main.home.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.fragment.tool.runRepeatedlyOnViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.main.MainInternalNavigator
import me.ztiany.wan.main.databinding.MainFragmentFeedBinding
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : BaseUIFragment<MainFragmentFeedBinding>() {

    @Inject internal lateinit var mainScopeNavigator: MainInternalNavigator

    private val viewModel by viewModels<FeedViewModule>()

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        subscribeViewModel()
    }

    private fun subscribeViewModel() = runRepeatedlyOnViewLifecycle {
        Timber.d("viewModel: $viewModel")
    }

}