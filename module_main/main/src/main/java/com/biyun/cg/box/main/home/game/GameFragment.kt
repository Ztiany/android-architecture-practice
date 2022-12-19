package com.biyun.cg.box.main.home.game

import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.biyun.cg.box.main.databinding.MainFragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint
import com.biyun.cg.box.main.home.MainNavigator
import javax.inject.Inject


@AndroidEntryPoint
class GameFragment : BaseUIFragment<MainFragmentGameBinding>() {

    @Inject lateinit var mainNavigator: MainNavigator

    private val viewModel by viewModels<GameViewModule>()

}
