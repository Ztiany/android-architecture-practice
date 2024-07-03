package me.ztiany.wan.main.home.mine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.fragment.base.BaseUIFragment
import com.app.base.utils.setStatusBarLightMode
import me.ztiany.wan.main.home.MainScopeNavigator
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.main.databinding.MainFragmentMineBinding
import javax.inject.Inject

/**
 *@author Ztiany
 */
@AndroidEntryPoint
class MineFragment : BaseUIFragment<MainFragmentMineBinding>() {

    private val viewModel: MineViewModel by viewModels()

    @Inject internal lateinit var mainScopeNavigator: MainScopeNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }


    override fun onResume() {
        super.onResume()
        requireActivity().setStatusBarLightMode()
    }

    private fun subscribeViewModel() {
        viewModel.userState.observe(this){

        }
    }

}