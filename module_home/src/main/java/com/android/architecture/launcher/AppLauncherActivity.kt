package com.android.architecture.launcher

import android.content.Intent
import android.os.Bundle
import com.app.base.AppContext
import com.app.base.app.AppBaseActivity
import com.app.base.router.AppRouter
import com.app.base.router.RouterPath
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 *启动页
 *
 *@author Ztiany
 *      Date : 2018-09-06 14:42
 */
@AndroidEntryPoint
class AppLauncherActivity : AppBaseActivity() {

    @Inject lateinit var appRouter: AppRouter

    override fun tintStatusBar() = false

    override fun provideLayout(): Nothing? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //https://medium.com/@elye.project/three-important-yet-unbeknown-android-app-launcher-behaviors-part-2-139a4d88157
        //https://issuetracker.google.com/issues/72299534
        if (!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null && intent.action == Intent.ACTION_MAIN) {
            finish()
        }
    }

    override fun setUpLayout(savedInstanceState: Bundle?) {
        super.setUpLayout(savedInstanceState)
        window.decorView.postDelayed({
            appRouter.build(RouterPath.Main.PATH).withTransition(0, 0).navigation()
            finishAfterTransition()
        }, 1000)
    }

}