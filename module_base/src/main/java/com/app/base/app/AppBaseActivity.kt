package com.app.base.app

import android.os.Bundle

import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import com.android.base.app.activity.BaseActivity
import com.android.base.rx.AutoDisposeLifecycleOwnerEx
import com.android.base.utils.android.compat.SystemBarCompat
import com.app.base.R
import com.app.base.config.AppSettings
import com.app.base.router.RouterManager

/**
 * @author Ztiany
 * Date : 2018-09-21 14:34
 */
abstract class AppBaseActivity : BaseActivity(), AutoDisposeLifecycleOwnerEx {

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        if (enableStatusBarLightMode()) {
            AppSettings.setSupportStatusBarLightMode(setStatusBarLightMode())
        }
        RouterManager.inject(this)
    }

    @CallSuper
    override fun setupView(savedInstanceState: Bundle?) {
        if (tintStatusBar()) {
            SystemBarCompat.setTranslucentStatusOn19(this)
            if (enableStatusBarLightMode() && AppSettings.supportStatusBarLightMode()) {
                SystemBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white))
            } else {
                SystemBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimaryDark))
            }
        }
    }

    protected open fun tintStatusBar() = true

    protected open fun enableStatusBarLightMode() = true

}