package com.vclusters.cloud.main.launch

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.android.base.utils.android.compat.SystemBarCompat
import com.app.base.app.AppBaseActivity
import com.app.base.app.CustomizeSystemBar
import com.app.base.router.AppRouter
import com.vclusters.cloud.main.R
import com.vclusters.cloud.main.api.MainModule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject


/**
 *启动页
 *
 *@author Ztiany
 *      Date : 2018-09-06 14:42
 */
@AndroidEntryPoint
class AppLaunchActivity : AppBaseActivity(), CustomizeSystemBar {

    @Inject lateinit var appRouter: AppRouter

    override fun provideLayout() = R.layout.main_activity_launch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarCompat.setTransparentSystemBarViaViewFlags(this)
        //https://medium.com/@elye.project/three-important-yet-unbeknown-android-app-launcher-behaviors-part-2-139a4d88157
        //https://issuetracker.google.com/issues/72299534
        if (!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null && intent.action == Intent.ACTION_MAIN) {
            finish()
        }
    }

    override fun setUpLayout(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenResumed {
            delay(1500)
            appRouter.build(MainModule.PATH).withTransition(0, 0).navigation()
            finishAfterTransition()
            overridePendingTransition(0, 0)
        }
    }


}