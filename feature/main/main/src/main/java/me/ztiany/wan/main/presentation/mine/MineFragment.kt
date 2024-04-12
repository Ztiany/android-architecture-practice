package me.ztiany.wan.main.presentation.mine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.fragment.base.BaseUIFragment
import com.app.base.utils.setStatusBarLightMode
import me.ztiany.wan.main.MainScopeNavigator
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

    private val uiPresenter by lazy { MineUIPresenter(this, vb) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) {
        super.onSetUpCreatedView(view, savedInstanceState)
        uiPresenter.initLayout()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setStatusBarLightMode()
    }

    private fun subscribeViewModel() {
        viewModel.userState.observe(this){
            uiPresenter.showUserInfo(it)
        }
    }

}