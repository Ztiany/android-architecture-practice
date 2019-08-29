package me.ztiany.arch.home.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.android.base.app.fragment.FragmentInfo
import com.android.base.app.fragment.TabManager
import com.android.base.utils.android.compat.SystemBarCompat
import com.app.base.app.InjectorAppBaseActivity
import com.app.base.router.RouterPath
import com.app.base.widget.dialog.TipsManager
import com.blankj.utilcode.util.AppUtils
import kotlinx.android.synthetic.main.main_activity.*
import me.ztiany.arch.home.main.index.presentation.IndexFragment
import me.ztiany.arch.home.main.middle.MiddleFragment
import me.ztiany.arch.home.main.mine.presentation.MineFragment
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

    private lateinit var tabManager: TabManager
    private var clickToExit = false

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
        tabManager = MainTabManager(this, supportFragmentManager, R.id.common_container_id)
        //ui初始化
        //设置 TranslucentStatus
        SystemBarCompat.setTranslucentSystemUi(this, true, false)
        //MainTable
        tabManager.setup(savedInstanceState)
        //bottomBar
        mainBottomBar.setOnTabSelectListener { tabId ->
            tabManager.selectTabById(tabId)
            onSelectTab(tabManager.currentPosition)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        tabManager.onSaveInstanceState(outState)
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
        if (clickToExit) {
            supportFinishAfterTransition()
        }
        if (!clickToExit) {
            clickToExit = true
            TipsManager.showMessage(getString(R.string.main_exit_tips))
            mainBottomBar.postDelayed({ clickToExit = false }, 1000)
        }
    }

    private fun onSelectTab(currentPosition: Int) {
        // no op
    }
}


private class MainTabManager(
        context: Context,
        fragmentManager: FragmentManager, containerId: Int
) : TabManager(context, fragmentManager, MainTabs(), containerId, SHOW_HIDE) {

    private class MainTabs internal constructor() : TabManager.Tabs() {
        init {
            add(FragmentInfo.PageBuilder()
                    .clazz(IndexFragment::class.java)
                    .tag(IndexFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_index)
                    .build())

            add(FragmentInfo.PageBuilder()
                    .clazz(MiddleFragment::class.java)
                    .tag(MiddleFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_middle)
                    .build())

            add(FragmentInfo.PageBuilder()
                    .clazz(MineFragment::class.java)
                    .tag(MineFragment::class.java.name)
                    .toStack(false)
                    .pagerId(R.id.main_mine)
                    .build())
        }
    }

}