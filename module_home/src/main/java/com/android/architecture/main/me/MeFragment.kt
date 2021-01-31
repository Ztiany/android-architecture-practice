package com.android.architecture.main.me

import androidx.fragment.app.viewModels
import com.android.architecture.main.MainNavigator
import com.android.architecture.main.R
import com.android.base.app.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:40
 */
@AndroidEntryPoint
class MeFragment : BaseFragment() {

    private val viewModel: MeViewModel by viewModels()

    @Inject lateinit var mainNavigator: MainNavigator

    override fun provideLayout() = R.layout.main_fragment_me

}