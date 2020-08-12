package me.ztiany.arch.home.main

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
import com.app.base.router.RouterPath
import com.app.base.widget.dialog.TipsManager
import com.blankj.utilcode.util.AppUtils
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment_root.*
import me.ztiany.architecture.home.R
import timber.log.Timber


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

    override fun layout() = R.layout.main_activity

    override fun tintStatusBar() = false

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        AppUtils.getAppSignature().forEach {
            Timber.d(it.toCharsString())
        }
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        super.initializeView(savedInstanceState)
        initViews(savedInstanceState)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        super.initializeView(savedInstanceState)
        //设置 TranslucentStatus
        SystemBarCompat.setTranslucentSystemUi(this, true, false)
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
        }
    }

    override fun superOnBackPressed() {
        if (clickToExit) {
            supportFinishAfterTransition()
        }
        if (!clickToExit) {
            clickToExit = true
            TipsManager.showMessage(getString(R.string.main_exit_tips))
            mainBottomBar.postDelayed({ clickToExit = false }, 1000)
        }

    }

}