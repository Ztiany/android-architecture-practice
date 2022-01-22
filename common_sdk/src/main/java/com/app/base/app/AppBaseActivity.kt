package com.app.base.app

import android.content.res.Configuration
import android.content.res.Resources
import com.android.base.architecture.activity.BaseActivity
import com.android.base.utils.android.TextSizeAdaptation

/** Defining a base activity may be useful in the future. */
abstract class AppBaseActivity : BaseActivity() {

    ///////////////////////////////////////////////////////////////////////////
    // 字体大小适配【忽略字体大小】
    ///////////////////////////////////////////////////////////////////////////
    private val screenAdaptation by lazy { TextSizeAdaptation(this) }

    override fun onConfigurationChanged(newConfig: Configuration) {
        screenAdaptation.onConfigurationChanged(newConfig)
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources {
        return screenAdaptation.fixResources(super.getResources())
    }

}