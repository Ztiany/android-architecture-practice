package me.ztiany.wan.main.home.box

import androidx.fragment.app.viewModels
import com.android.base.fragment.base.BaseUIFragment
import com.app.base.utils.setStatusBarLightMode
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.main.databinding.MainFragmentBoxBinding
import me.ztiany.wan.main.home.MainScopeNavigator
import javax.inject.Inject

@AndroidEntryPoint
class BoxFragment : BaseUIFragment<MainFragmentBoxBinding>() {

    private val viewModel by viewModels<BoxViewModel>()

    @Inject internal lateinit var mainScopeNavigator: MainScopeNavigator

    override fun onResume() {
        super.onResume()
        requireActivity().setStatusBarLightMode()
    }

}