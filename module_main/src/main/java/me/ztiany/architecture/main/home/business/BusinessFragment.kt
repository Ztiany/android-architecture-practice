package me.ztiany.architecture.main.home.business

import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.architecture.main.databinding.MainFragmentBusinessBinding
import me.ztiany.architecture.main.home.MainNavigator
import javax.inject.Inject


@AndroidEntryPoint
class BusinessFragment : BaseUIFragment<MainFragmentBusinessBinding>() {

    @Inject lateinit var mainNavigator: MainNavigator

    private val viewModel by viewModels<BusinessViewModule>()

}
