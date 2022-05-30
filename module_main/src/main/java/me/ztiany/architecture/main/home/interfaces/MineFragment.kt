package me.ztiany.architecture.main.home.interfaces

import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.architecture.main.databinding.MainFragmentInterfaceBinding
import me.ztiany.architecture.main.home.MainNavigator
import javax.inject.Inject

/**
 *@author Ztiany
 */
@AndroidEntryPoint
class MineFragment : BaseUIFragment<MainFragmentInterfaceBinding>() {

    private val viewModel: MineViewModel by viewModels()

    @Inject lateinit var mainNavigator: MainNavigator

}