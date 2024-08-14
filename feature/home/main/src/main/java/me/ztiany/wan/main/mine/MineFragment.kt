package me.ztiany.wan.main.mine

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.android.base.fragment.base.BaseUIFragment
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.main.MainInternalNavigator
import me.ztiany.wan.main.databinding.MainFragmentMineBinding
import javax.inject.Inject

/**
 *@author Ztiany
 */
@AndroidEntryPoint
class MineFragment : BaseUIFragment<MainFragmentMineBinding>() {

    private val viewModel: MineViewModel by viewModels()

    @Inject internal lateinit var mainScopeNavigator: MainInternalNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    private fun subscribeViewModel() {
        viewModel.userState.observe(this) {

        }
    }

}