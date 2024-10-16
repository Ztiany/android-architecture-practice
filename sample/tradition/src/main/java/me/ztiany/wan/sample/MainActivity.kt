package me.ztiany.wan.sample

import android.os.Bundle
import com.android.base.fragment.tool.doFragmentTransaction
import com.android.base.fragment.tool.findFragmentByTag
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import com.app.base.app.AppBaseActivity
import com.app.base.ui.widget.insets.enableEdgeToEdgeCompatible
import com.app.base.widget.dialog.toast.ToastKit
import com.app.common.api.protocol.CustomizeSystemBar
import dagger.hilt.android.AndroidEntryPoint


/**
 * 演示模块【基于 View 架构】
 *
 *@author Ztiany
 */
@AndroidEntryPoint
class MainActivity : AppBaseActivity(), CustomizeSystemBar {

    private var clickToExit = false

    private lateinit var mainFragment: MainFragment

    override fun initialize(savedInstanceState: Bundle?) {
        enableEdgeToEdgeCompatible()
    }

    override fun provideLayout() = R.layout.sample_activity_main

    override fun setUpLayout(savedInstanceState: Bundle?) {
        supportFragmentManager.findFragmentByTag<MainFragment>().ifNonNull {
            mainFragment = this@ifNonNull
        } otherwise {
            mainFragment = MainFragment()
            doFragmentTransaction {
                addFragment(mainFragment)
            }
        }
    }

    override fun handleOnBackPressed() {
        if (!mainFragment.isVisible) {
            super.handleOnBackPressed()
            return
        }
        if (clickToExit) {
            supportFinishAfterTransition()
        }
        if (!clickToExit) {
            clickToExit = true
            ToastKit.showMessage(this, getString(R.string.sample_exit_tips))
            window.decorView.postDelayed({
                clickToExit = false
            }, 1000)
        }
    }

}