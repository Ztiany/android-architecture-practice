package me.ztiany.wan.main.mine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.utils.android.views.onThrottledClick
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

    @Inject internal lateinit var navigator: MainInternalNavigator

    override fun onSetupCreatedView(view: View, savedInstanceState: Bundle?) = withVB {
        tvLogin.onThrottledClick {
            navigator.toLogin()
        }
    }

}