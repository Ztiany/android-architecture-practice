package me.ztiany.wan.###template###.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import me.ztiany.wan.###template###.databinding.$$$template$$$FragmentBinding
import me.ztiany.wan.###template###.$$$template$$$InternalNavigator
import com.android.base.fragment.base.BaseUIFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class $$$template$$$Fragment : BaseUIFragment<$$$template$$$FragmentBinding>() {

    private val viewModel by viewModels<$$$template$$$ViewModel>()

    @Inject internal lateinit var navigator: $$$template$$$InternalNavigator

    override fun onSetupCreatedView(view: View, savedInstanceState: Bundle?) = withVB {

    }

}