package me.ztiany.architecture.main.launch

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.android.base.utils.android.compat.SystemBarCompat
import com.app.base.app.AppBaseActivity
import com.app.base.app.CustomizeSystemBar
import com.app.base.component.usermanager.UserManager
import com.app.base.component.usermanager.isUserLogin
import com.app.base.router.AppRouter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import me.ztiany.architecture.account.api.AccountModuleNavigator
import me.ztiany.architecture.main.R
import me.ztiany.architecture.main.api.MainModuleNavigator
import javax.inject.Inject

/**
 * 应用启动页
 *
 *@author Ztiany
 *      Date : 2018-09-06 14:42
 */
@AndroidEntryPoint
class AppLaunchActivity : AppBaseActivity(), CustomizeSystemBar {

    @Inject lateinit var appRouter: AppRouter

    @Inject lateinit var userManager: UserManager

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
            if (userManager.isUserLogin()) {
                toMainPage()
            } else {
                toLoginPage()
            }
            finishAfterTransition()
            overridePendingTransition(0, 0)
        }
    }

    private fun toLoginPage() {
        appRouter.get(AccountModuleNavigator::class.java)?.openAccount(this)
        overridePendingTransition(0, 0)
    }

    private suspend fun toMainPage() {
        delay(1500)
        appRouter.get(MainModuleNavigator::class.java)?.openMain(this)
    }

}