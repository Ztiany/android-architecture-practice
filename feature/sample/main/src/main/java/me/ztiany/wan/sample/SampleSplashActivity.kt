package me.ztiany.wan.sample

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.android.base.utils.android.compat.SystemBarCompat
import com.app.base.app.AppBaseActivity
import com.app.common.api.protocol.CustomizeSystemBar
import com.app.base.utils.setNavigatorBarColorLightest
import com.app.common.api.router.AppRouter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 应用启动页
 *
 *@author Ztiany
 */
@AndroidEntryPoint
class SampleSplashActivity : AppBaseActivity(), CustomizeSystemBar {

    @Inject lateinit var appRouter: AppRouter

    override fun provideLayout(): Any? = null

    override fun setUpLayout(savedInstanceState: Bundle?) = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarCompat.setExtendsToSystemBar(this, true)
        setNavigatorBarColorLightest()
        SystemBarCompat.displayInNotch(this)
        //https://medium.com/@elye.project/three-important-yet-unbeknown-android-app-launcher-behaviors-part-2-139a4d88157
        //https://issuetracker.google.com/issues/72299534
        if (!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null && intent.action == Intent.ACTION_MAIN) {
            finish()
        } else {
            startActivity(Intent(this, SampleActivity::class.java))
            finishSelf()
        }
    }

    private fun finishSelf() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }
        finish()
    }

}