package me.ztiany.wan.main.home.treasure

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.utils.android.views.onDebouncedClick
import com.app.base.utils.setStatusBarLightMode
import com.app.base.widget.dialog.showConfirmDialog
import me.ztiany.wan.main.home.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.main.databinding.MainFragmentWelfareBinding
import javax.inject.Inject

@AndroidEntryPoint
class WelfareFragment : BaseUIFragment<MainFragmentWelfareBinding>() {

    private val viewModel by viewModels<WelfareViewModel>()

    @Inject lateinit var mainNavigator: MainNavigator

    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) {
        super.onSetUpCreatedView(view, savedInstanceState)
        vb.mainTvSign.onDebouncedClick {
            showConfirmDialog {
                title = "温馨提示"
                message = "功能开发中，敬请期待"
                disableNegative()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setStatusBarLightMode()
    }

}