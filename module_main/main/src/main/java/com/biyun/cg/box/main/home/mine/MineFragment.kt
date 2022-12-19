package com.biyun.cg.box.main.home.mine

import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.biyun.cg.box.main.databinding.MainFragmentMineBinding
import dagger.hilt.android.AndroidEntryPoint
import com.biyun.cg.box.main.home.MainNavigator
import javax.inject.Inject

/**
 *@author Ztiany
 */
@AndroidEntryPoint
class MineFragment : BaseUIFragment<MainFragmentMineBinding>() {

    private val viewModel: MineViewModel by viewModels()

    @Inject lateinit var mainNavigator: MainNavigator

}