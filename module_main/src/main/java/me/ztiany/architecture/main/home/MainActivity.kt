package me.ztiany.architecture.main.home

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.android.base.architecture.fragment.tools.clearBackStack
import com.android.base.architecture.fragment.tools.doFragmentTransaction
import com.android.base.architecture.fragment.tools.findFragmentByTag
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.ignoreCrash
import com.android.base.utils.common.otherwise
import com.android.common.api.usermanager.UserManager
import com.app.base.app.AppBaseActivity
import com.app.base.widget.dialog.TipsTool
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ztiany.architecture.main.R
import me.ztiany.architecture.main.ACTION_KEY
import me.ztiany.architecture.main.ACTION_RE_LOGIN
import me.ztiany.architecture.main.PAGE_KEY
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

    override fun provideLayout() = R.layout.main_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
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
            //用户需要重新登录，登录之前需要清空用户之前的数据
            if (action == ACTION_RE_LOGIN) {
                //back to main
                supportFragmentManager.clearBackStack()
                postRun {
                    mainFragment.selectTabAtPosition(0)
                    userManager.logout()
                    //jump to login activity
                    mainNavigator.toLogin()
                    //finish self
                    supportFinishAfterTransition()
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
            TipsTool.showMessage(this, getString(R.string.main_exit_tips))
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

}