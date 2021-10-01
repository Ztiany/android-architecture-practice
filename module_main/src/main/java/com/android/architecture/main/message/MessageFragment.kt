package com.android.architecture.main.message

import com.android.architecture.main.MainNavigator
import com.android.architecture.main.common.MainEvents
import com.android.architecture.main.databinding.MainFragmentMessageBinding
import com.android.base.architecture.fragment.base.BaseUIFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessageFragment : BaseUIFragment<MainFragmentMessageBinding>() {

    @Inject lateinit var mainNavigator: MainNavigator

    @Inject lateinit var mainEvents: MainEvents

}