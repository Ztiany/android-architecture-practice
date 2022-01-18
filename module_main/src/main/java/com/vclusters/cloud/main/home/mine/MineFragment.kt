package com.vclusters.cloud.main.home.mine

import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.vclusters.cloud.main.databinding.MainFragmentMineBinding
import com.vclusters.cloud.main.home.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:40
 */
@AndroidEntryPoint
class MineFragment : BaseUIFragment<MainFragmentMineBinding>() {

    private val viewModel: MineViewModel by viewModels()

    @Inject lateinit var mainNavigator: MainNavigator

}