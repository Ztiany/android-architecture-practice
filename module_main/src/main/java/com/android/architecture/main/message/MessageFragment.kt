package com.android.architecture.main.message

import com.android.architecture.main.MainNavigator
import com.android.architecture.main.R
import com.android.architecture.main.common.MainEvents
import com.android.architecture.main.databinding.MainFragmentMessageBinding
import com.android.base.app.fragment.BaseUIFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessageFragment : BaseUIFragment() {

    @Inject lateinit var mainNavigator: MainNavigator

    @Inject lateinit var mainEvents: MainEvents

    override fun provideLayout() = R.layout.main_fragment_message

}