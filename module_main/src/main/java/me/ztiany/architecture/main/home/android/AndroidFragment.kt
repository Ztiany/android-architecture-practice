package me.ztiany.architecture.main.home.android

import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.architecture.main.databinding.MainFragmentAndroidBinding
import me.ztiany.architecture.main.home.MainNavigator
import javax.inject.Inject

@AndroidEntryPoint
class AndroidFragment : BaseUIFragment<MainFragmentAndroidBinding>() {

    private val viewModel by viewModels<AndroidViewModel>()

    @Inject lateinit var mainNavigator: MainNavigator

}