package com.vclusters.cloud.main.launch

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.android.base.architecture.activity.BaseActivity
import com.app.base.app.CustomizeSystemBar
import com.app.base.router.AppRouter
import com.vclusters.cloud.main.api.MainModule
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 *启动页
 *
 *@author Ztiany
 *      Date : 2018-09-06 14:42
 */
@AndroidEntryPoint
class AppLaunchActivity : BaseActivity(), CustomizeSystemBar {

    @Inject lateinit var appRouter: AppRouter

    override fun provideLayout(): Nothing? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //https://medium.com/@elye.project/three-important-yet-unbeknown-android-app-launcher-behaviors-part-2-139a4d88157
        //https://issuetracker.google.com/issues/72299534
        if (!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null && intent.action == Intent.ACTION_MAIN) {
            finish()
        }

        setContent {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
            ) {
                Text("Android Architecture Practice", Modifier.align(Alignment.Center))
            }
        }

    }

    override fun setUpLayout(savedInstanceState: Bundle?) {
        window.decorView.postDelayed({
            appRouter.build(MainModule.PATH).withTransition(0, 0).navigation()
            finishAfterTransition()
        }, 1500)
    }

}