package com.biyun.cg.box.main.home.welfare

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.utils.android.views.onDebouncedClick
import com.biyun.cg.box.main.databinding.MainFragmentWelfareBinding
import dagger.hilt.android.AndroidEntryPoint
import com.biyun.cg.box.main.home.MainNavigator
import javax.inject.Inject

@AndroidEntryPoint
class WelfareFragment : BaseUIFragment<MainFragmentWelfareBinding>() {

    private val viewModel by viewModels<WelfareViewModel>()

    @Inject lateinit var mainNavigator: MainNavigator

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        vb.mainTvSign.onDebouncedClick {
            showMessage("开发中...")
        }
    }

}