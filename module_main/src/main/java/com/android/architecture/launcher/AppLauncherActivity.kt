package com.android.architecture.launcher

import android.content.Intent
import android.os.Bundle
import com.android.architecture.main.api.MainModule
import com.android.base.app.activity.BaseActivity
import com.app.base.app.CustomizeSystemBar
import com.app.base.router.AppRouter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 *启动页
 *
 *@author Ztiany
 *      Date : 2018-09-06 14:42
 */
@AndroidEntryPoint
class AppLauncherActivity : BaseActivity(), CustomizeSystemBar {

    @Inject lateinit var appRouter: AppRouter

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
        window.decorView.postDelayed({
            appRouter.build(MainModule.PATH).withTransition(0, 0).navigation()
            finishAfterTransition()
        }, 1000)
    }

}