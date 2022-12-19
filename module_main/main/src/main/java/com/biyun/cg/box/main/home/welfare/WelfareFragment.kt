package com.biyun.cg.box.main.home.welfare

import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.biyun.cg.box.main.databinding.MainFragmentWelfareBinding
import dagger.hilt.android.AndroidEntryPoint
import com.biyun.cg.box.main.home.MainNavigator
import javax.inject.Inject

@AndroidEntryPoint
class WelfareFragment : BaseUIFragment<MainFragmentWelfareBinding>() {

    private val viewModel by viewModels<WelfareViewModel>()

    @Inject lateinit var mainNavigator: MainNavigator

}