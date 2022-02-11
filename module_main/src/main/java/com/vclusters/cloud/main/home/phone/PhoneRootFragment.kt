package com.vclusters.cloud.main.home.phone

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.utils.common.timing
import com.vclusters.cloud.main.R
import com.vclusters.cloud.main.databinding.MainFragmentCloudPhoneRootBinding
import com.vclusters.cloud.main.home.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class PhoneRootFragment : BaseUIFragment<MainFragmentCloudPhoneRootBinding>() {

    @Inject lateinit var mainNavigator: MainNavigator

    private val refreshMessageCountTiming by timing(5.seconds.inWholeMilliseconds)

    private val tabManager by lazy {
        PhoneTabManager(requireContext(), childFragmentManager, R.id.main_fl_phone_container)
    }

    private val viewModel by viewModels<PhoneRootViewModule>()

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
            mainNavigator.openMessageCenter()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tabManager.onSaveInstanceState(outState)
    }

    private fun subscribeViewModel() {
        viewModel.messageCount.observe(this, {
            vb.mainPhoneTitleBar.showMessageCount(it)
        })
    }

    override fun onResume() {
        super.onResume()
        if (refreshMessageCountTiming) {
            viewModel.queryMessageCount()
        }
    }

}
