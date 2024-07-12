package me.ztiany.wan.sample

import android.graphics.Color
import android.os.Bundle
import com.android.base.fragment.tool.doFragmentTransaction
import com.android.base.fragment.tool.findFragmentByTag
import com.android.base.utils.android.compat.SystemBarCompat
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import com.app.base.app.AppBaseActivity
import com.app.base.utils.setNavigatorBarColorLightest
import com.app.base.widget.dialog.ToastKit
import dagger.hilt.android.AndroidEntryPoint


/**
 *演示界面
 *
 *@author Ztiany
 */
@AndroidEntryPoint
class SampleActivity : AppBaseActivity() {

    private var clickToExit = false

    private lateinit var mainFragment: MainFragment

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        SystemBarCompat.setExtendsToSystemBar(this, true)
        SystemBarCompat.setStatusBarColor(this, Color.TRANSPARENT)
        setNavigatorBarColorLightest()
    }

    override fun provideLayout() = R.layout.sample_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        initViews()
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