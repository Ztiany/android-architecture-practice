package me.ztiany.wan.main.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.android.base.fragment.tool.clearBackStack
import com.android.base.fragment.tool.doFragmentTransaction
import com.android.base.fragment.tool.findFragmentByTag
import com.android.base.utils.android.compat.SystemBarCompat
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.ignoreCrash
import com.android.base.utils.common.otherwise
import com.android.sdk.upgrade.AppUpgradeChecker
import com.app.base.app.AppBaseActivity
import com.app.base.utils.setNavigatorBarColorLightest
import com.app.base.widget.dialog.ToastKit
import com.app.common.api.usermanager.UserManager
import me.ztiany.wan.main.ACTION_KEY
import me.ztiany.wan.main.ACTION_RE_LOGIN
import me.ztiany.wan.main.PAGE_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ztiany.wan.main.R
import javax.inject.Inject


/**
 *主界面
 *
 *@author Ztiany
 */
@AndroidEntryPoint
class MainActivity : AppBaseActivity() {

    private var clickToExit = false

    private lateinit var mainFragment: MainFragment

    @Inject lateinit var mainNavigator: MainNavigator

    @Inject lateinit var userManager: UserManager

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        SystemBarCompat.setExtendsToSystemBar(this, true)
        SystemBarCompat.setStatusBarColor(this, Color.TRANSPARENT)
        setNavigatorBarColorLightest()
    }

    override fun provideLayout() = R.layout.main_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        initViews()
        window.decorView.post {
            ignoreCrash { processIntent(intent) }
        }
    }

    private fun initViews() {
        //设置 MainFragment
        supportFragmentManager.findFragmentByTag<MainFragment>().ifNonNull {
            mainFragment = this@ifNonNull
        } otherwise {
            mainFragment = MainFragment()
            doFragmentTransaction {
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
        if (intent.hasExtra(PAGE_KEY)) {
            val page = intent.getIntExtra(PAGE_KEY, 0)
            supportFragmentManager.clearBackStack()
            mainFragment.selectTabAtPosition(page)
            return
        }

        //其他行为
        if (intent.hasExtra(ACTION_KEY)) {
            val action = intent.getIntExtra(ACTION_KEY, 0)
            //用户需要重新登录，登录之前需要清空用户之前的数据。
            if (action == ACTION_RE_LOGIN) {
                //back to main
                supportFragmentManager.clearBackStack()
                postRun {
                    mainFragment.selectTabAtPosition(0)
                    userManager.logout()
                    //jump to login activity
                    mainNavigator.toLogin()
                }
            }
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
            ToastKit.showMessage(this, getString(R.string.main_exit_tips))
            window.decorView.postDelayed({
                clickToExit = false
            }, 1000)
        }
    }

    private fun postRun(delaMillis: Long = 0, action: () -> Unit) {
        lifecycleScope.launch {
            delay(delaMillis)
            action()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //postRun(1000) { AppUpgradeChecker.checkAppUpgrade() }
    }

}