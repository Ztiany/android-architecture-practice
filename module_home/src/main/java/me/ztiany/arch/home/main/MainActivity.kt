package me.ztiany.arch.home.main

import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.android.base.app.fragment.tools.TabManager
import com.app.base.app.InjectorAppBaseActivity
import com.app.base.router.RouterPath
import com.app.base.widget.dialog.TipsManager
import com.blankj.utilcode.util.AppUtils
import kotlinx.android.synthetic.main.main_activity.*
import me.ztiany.architecture.home.R
import timber.log.Timber


/**
 *主界面
 *
 *@author Ztiany
 *      Date : 2018-09-06 14:42
 */
@Route(path = RouterPath.Main.PATH)
class MainActivity : InjectorAppBaseActivity() {

    private lateinit var mTabManager: TabManager
    private var mClickToExit = false

    override fun layout() = R.layout.main_activity

    override fun tintStatusBar() = false

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        AppUtils.getAppSignature().forEach {
            Timber.d(it.toCharsString())
        }
    }

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        initViews(savedInstanceState)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        mTabManager = MainTabManager(this, supportFragmentManager, R.id.common_container_id)
        //ui初始化
        TranslucentHelper(this).setTranslucentStatus()
        //MainTable
        mTabManager.setup(savedInstanceState)
        //bottomBar
        mainBottomBar.setOnTabSelectListener { tabId ->
            mTabManager.selectTabById(tabId)
            onSelectTab(mTabManager.currentPosition)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mTabManager.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val page = intent.getIntExtra(RouterPath.Main.PAGE_KEY, 0)
        if (page in 0..3) {
            mainBottomBar.post { mainBottomBar.selectTabAtPosition(page, true) }
        }
    }

    override fun processBackPressed() {
        if (mClickToExit) {
            supportFinishAfterTransition()
        }
        if (!mClickToExit) {
            mClickToExit = true
            TipsManager.showMessage(getString(R.string.main_exit_tips))
            mainBottomBar.postDelayed({ mClickToExit = false }, 1000)
        }
    }

    private fun onSelectTab(currentPosition: Int) {
        // no op
    }
}