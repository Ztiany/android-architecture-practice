package com.android.architecture.main.feed

import android.os.Bundle
import com.android.architecture.main.MainFragment
import com.android.architecture.main.MainNavigator
import com.android.architecture.main.R
import com.android.base.app.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FeedFragment : BaseFragment(), MainFragment.MainFragmentChild {

    @Inject lateinit var mainNavigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun provideLayout() = R.layout.main_fragment_feed

    private fun subscribeViewModel() {

    }

}