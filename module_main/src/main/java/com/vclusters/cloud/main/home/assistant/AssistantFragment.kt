package com.vclusters.cloud.main.home.assistant

import com.android.base.architecture.fragment.base.BaseUIFragment
import com.vclusters.cloud.main.databinding.MainFragmentAssistantBinding
import com.vclusters.cloud.main.home.MainNavigator
import com.vclusters.cloud.main.home.common.MainEvents
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AssistantFragment : BaseUIFragment<MainFragmentAssistantBinding>() {

    @Inject lateinit var mainNavigator: MainNavigator

    @Inject lateinit var mainEvents: MainEvents

}