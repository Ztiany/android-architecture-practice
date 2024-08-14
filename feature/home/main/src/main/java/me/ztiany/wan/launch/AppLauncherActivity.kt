package me.ztiany.wan.launch

import android.content.Intent
import android.os.Bundle
import com.app.base.app.AppBaseActivity
import com.app.common.api.router.AppRouter
import com.app.common.api.router.withNavigator
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.main.MainModuleNavigator
import javax.inject.Inject

/**
 * 应用启动页
 *
 *@author Ztiany
 */
@AndroidEntryPoint
class AppLauncherActivity : AppBaseActivity() {

    @Inject lateinit var appRouter: AppRouter

    override fun setUpLayout(savedInstanceState: Bundle?) {
        //https://medium.com/@elye.project/three-important-yet-unbeknown-android-app-launcher-behaviors-part-2-139a4d88157
        //https://issuetracker.google.com/issues/72299534
        if (!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }

        appRouter.withNavigator<MainModuleNavigator> { openMain(this@AppLauncherActivity) }
        overridePendingTransition(0, 0)
        finish()
    }

}