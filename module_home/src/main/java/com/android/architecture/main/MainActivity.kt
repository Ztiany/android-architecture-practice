package com.android.architecture.main

import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.android.base.app.fragment.tools.clearBackStack
import com.android.base.app.fragment.tools.findFragmentByTag
import com.android.base.app.fragment.tools.inFragmentTransaction
import com.android.base.utils.android.compat.SystemBarCompat
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.ignoreCrash
import com.android.base.utils.common.otherwise
import com.app.base.app.AppBaseActivity
import com.app.base.app.setStatusBarLightMode
import com.app.base.data.app.AppDataSource
import com.app.base.router.RouterManager
import com.app.base.router.RouterPath
import com.app.base.widget.dialog.TipsManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 *主界面
 *
 *@author Ztiany
 *      Date : 2018-09-06 14:42
 */
@Route(path = RouterPath.Main.PATH)
@AndroidEntryPoint
class MainActivity : AppBaseActivity() {

    private var clickToExit = false

    private lateinit var mainFragment: MainFragment

    @Inject lateinit var mainNavigator: MainNavigator

    @Inject lateinit var appDataSource: AppDataSource

    override fun provideLayout() = R.layout.main_activity

    override fun tintStatusBar() = false

    override fun hasRouterParams() = false

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        SystemBarCompat.setTransparentStatusViaViewFlags(this)
        setStatusBarLightMode()
    }

    override fun setUpLayout(savedInstanceState: Bundle?) {
        super.setUpLayout(savedInstanceState)
        initViews()
        window.decorView.post {
            ignoreCrash { processIntent(intent) }
        }
    }

    private fun initViews() {
        //设置 MainFragment
        supportFragmentManager.findFragmentByTag(MainFragment::class).ifNonNull {
            mainFragment = this@ifNonNull
        }.otherwise {
            mainFragment = MainFragment()
            inFragmentTransaction {
                addFragment(mainFragment)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        window.decorView.post {
            ignoreCrash { processIntent(intent) }
        }
    }

    private fun processIntent(intent: Intent) {
        //切换页面
        if (intent.hasExtra(RouterPath.PAGE_KEY)) {
            val page = intent.getIntExtra(RouterPath.PAGE_KEY, 0)
            supportFragmentManager.clearBackStack()
            mainFragment.selectTabAtPosition(page)
            return
        }
    }

    override fun superOnBackPressed() {
        if (!mainFragment.isVisible) {
            super.superOnBackPressed()
            return
        }
        if (clickToExit) {
            supportFinishAfterTransition()
        }
        if (!clickToExit) {
            clickToExit = true
            TipsManager.showMessage(getString(R.string.main_exit_tips))
            window.decorView.postDelayed({
                clickToExit = false
            }, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        RouterManager.dispatchActivityResult(supportFragmentManager, requestCode, resultCode, data)
    }

}