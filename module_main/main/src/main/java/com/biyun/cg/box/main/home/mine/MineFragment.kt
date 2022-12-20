package com.biyun.cg.box.main.home.mine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.biyun.cg.box.main.databinding.MainFragmentMineBinding
import dagger.hilt.android.AndroidEntryPoint
import com.biyun.cg.box.main.home.MainNavigator
import javax.inject.Inject

/**
 *@author Ztiany
 */
@AndroidEntryPoint
class MineFragment : BaseUIFragment<MainFragmentMineBinding>() {

    private val viewModel: MineViewModel by viewModels()

    @Inject lateinit var mainNavigator: MainNavigator

    private val uiPresenter by lazy { MineUIPresenter(this, vb) }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        uiPresenter.initLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}