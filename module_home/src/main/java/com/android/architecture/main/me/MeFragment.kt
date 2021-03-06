package com.android.architecture.main.me

import androidx.fragment.app.viewModels
import com.android.architecture.main.MainNavigator
import com.android.architecture.main.databinding.MainFragmentMeBinding
import com.android.base.app.fragment.BaseUIFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:40
 */
@AndroidEntryPoint
class MeFragment : BaseUIFragment<MainFragmentMeBinding>() {

    private val viewModel: MeViewModel by viewModels()

    @Inject lateinit var mainNavigator: MainNavigator

}