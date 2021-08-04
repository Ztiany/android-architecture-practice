package com.app.base.app

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.android.base.app.activity.BaseActivity
import com.android.base.utils.android.ScreenAdaptation
import com.android.base.utils.android.compat.SystemBarCompat
import com.app.base.R
import com.app.base.config.AppSettings

/**
 * @author Ztiany
 * Date : 2018-09-21 14:34
 */
abstract class AppBaseActivity : BaseActivity() {

    private val screenAdaptation by lazy { ScreenAdaptation(this) }

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        if (hasRouterParams()) {
            ARouter.getInstance().inject(this)
        }
    }

    @CallSuper
    override fun setUpLayout(savedInstanceState: Bundle?) {
        if (tintStatusBar()) {
            SystemBarCompat.setTranslucentStatusOn19(this)
            AppSettings.supportStatusBarLightMode = setStatusBarLightMode()
            if (AppSettings.supportStatusBarLightMode) {
                SystemBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white))
            } else {
                SystemBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.opacity60_white))
            }
        }
    }

    protected open fun tintStatusBar() = true

    protected open fun hasRouterParams() = true

    override fun onConfigurationChanged(newConfig: Configuration) {
        screenAdaptation.onConfigurationChanged(newConfig)
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources {
        return screenAdaptation.fixResources(super.getResources())
    }

}