package com.android.architecture.main.feed

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.android.architecture.main.MainFragment
import com.android.architecture.main.MainNavigator
import com.android.architecture.main.R
import com.android.architecture.main.databinding.MainFragmentFeedBinding
import com.android.base.app.fragment.BaseUIFragment
import com.android.base.app.ui.viewBinding
import com.android.base.rx.autodispose.bindLifecycle
import com.android.sdk.net.coroutines.apiCall
import com.android.sdk.net.coroutines.apiCallRetry
import com.android.sdk.net.coroutines.onFailed
import com.android.sdk.net.coroutines.onSucceeded
import com.android.sdk.net.rxjava.resultExtractor
import com.app.base.app.ErrorHandler
import com.app.base.app.ServiceProvider
import com.app.base.databinding.BaseFragmentDebugBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class FeedFragment : BaseUIFragment(), MainFragment.MainFragmentChild {

    @Inject lateinit var mainNavigator: MainNavigator

    @Inject lateinit var errorHandler: ErrorHandler

    @Inject lateinit var serviceProvider: ServiceProvider

    private val layout by viewBinding(MainFragmentFeedBinding::bind)

    override fun provideLayout() = R.layout.main_fragment_feed

    private lateinit var api: TestAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = serviceProvider.getDefault().create(TestAPI::class.java)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        layout.feedBtnLoad.setOnClickListener {
            lifecycleScope.launch {
                apiCallRetry {
                    api.loadFirstKT()
                } onFailed {
                    Timber.e(it)
                } onSucceeded {
                    Timber.e(it.toString())
                }
            }
        }
    }

    private fun subscribeViewModel() {

    }

}