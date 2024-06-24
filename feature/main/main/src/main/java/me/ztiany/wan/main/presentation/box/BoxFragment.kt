package me.ztiany.wan.main.presentation.box

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.utils.android.views.onThrottledClick
import com.app.base.utils.setStatusBarLightMode
import com.app.base.widget.dialog.confirm.showConfirmDialog
import me.ztiany.wan.main.MainScopeNavigator
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.main.databinding.MainFragmentBoxBinding
import javax.inject.Inject

@AndroidEntryPoint
class BoxFragment : BaseUIFragment<MainFragmentBoxBinding>() {

    private val viewModel by viewModels<BoxViewModel>()

    @Inject internal lateinit var mainScopeNavigator: MainScopeNavigator

    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) {
        super.onSetUpCreatedView(view, savedInstanceState)
        vb.mainTvSign.onThrottledClick {
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