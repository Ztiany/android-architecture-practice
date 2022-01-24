package com.vclusters.cloud.main.home.phone

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.sdk.net.coroutines.nullable.apiCallRetryNullable
import com.android.sdk.net.coroutines.onFailed
import com.android.sdk.net.coroutines.onSucceeded
import com.app.base.app.ErrorHandler
import com.app.base.app.ServiceProvider
import com.vclusters.cloud.main.databinding.MainFragmentCloudPhoneRootBinding
import com.vclusters.cloud.main.home.MainFragment
import com.vclusters.cloud.main.home.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class PhoneRootFragment : BaseUIFragment<MainFragmentCloudPhoneRootBinding>(), MainFragment.MainFragmentChild {

    @Inject lateinit var mainNavigator: MainNavigator

    @Inject lateinit var errorHandler: ErrorHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)

        vb.mainBtnLogin.setOnClickListener {
            mainNavigator.toLogin()
        }

        vb.mainBtnSwitch.setOnClickListener {
            mainNavigator.toSwitchAccount()
        }

    }

    private fun subscribeViewModel() {

    }

}