package com.vclusters.cloud.main.home.phone

import android.os.Bundle
import android.view.View
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.app.base.app.ErrorHandler
import com.vclusters.cloud.main.R
import com.vclusters.cloud.main.databinding.MainFragmentCloudPhoneRootBinding
import com.vclusters.cloud.main.home.MainFragment
import com.vclusters.cloud.main.home.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PhoneRootFragment : BaseUIFragment<MainFragmentCloudPhoneRootBinding>(), MainFragment.MainFragmentChild {

    @Inject lateinit var mainNavigator: MainNavigator

    @Inject lateinit var errorHandler: ErrorHandler

    private val tabManager by lazy {
        PhoneTabManager(requireContext(), childFragmentManager, R.id.main_fl_phone_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        tabManager.setup(savedInstanceState)

        vb.mainPhoneTitleBar.onTabSelectedListener = {
            tabManager.selectTabByPosition(it)
        }

        vb.mainPhoneTitleBar.onMessageClickListener = View.OnClickListener {

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tabManager.onSaveInstanceState(outState)
    }

    private fun subscribeViewModel() {

    }

}
