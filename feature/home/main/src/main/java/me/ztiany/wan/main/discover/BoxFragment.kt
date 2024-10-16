package me.ztiany.wan.main.discover

import androidx.fragment.app.viewModels
import com.android.base.fragment.base.BaseUIFragment
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.main.MainInternalNavigator
import me.ztiany.wan.main.databinding.MainFragmentBoxBinding
import javax.inject.Inject

@AndroidEntryPoint
class BoxFragment : BaseUIFragment<MainFragmentBoxBinding>() {

    private val viewModel by viewModels<BoxViewModel>()

    @Inject internal lateinit var mainScopeNavigator: MainInternalNavigator

}