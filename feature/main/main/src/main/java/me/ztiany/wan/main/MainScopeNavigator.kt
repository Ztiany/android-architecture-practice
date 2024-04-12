package me.ztiany.wan.main

import android.content.Context
import com.app.base.common.web.BrowserStarter
import com.app.common.api.router.AppRouter
import com.app.common.api.usermanager.UserManager
import com.app.common.api.usermanager.isUserLogin
import me.ztiany.wan.account.api.AccountModuleNavigator
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 *@author Ztiany
 */
@ActivityScoped
internal class MainScopeNavigator @Inject constructor(
    @ActivityContext context: Context,
    private val appRouter: AppRouter,
    private val userManager: UserManager,
) {

    companion object {
        const val GAME_DETAIL_KEY = "detail_key"
    }

    private val host = context as MainActivity

    fun toLogin() {
        appRouter.getNavigator(AccountModuleNavigator::class.java)?.openAccount(host)
    }

    fun openSettings() {

    }

    fun openFeedback() {
        if (loginIfNot()) {

        }
    }

    fun checkRechargeRecords() {
        if (loginIfNot()) {

        }
    }

    fun showGameDetail(gameId: Int) {

    }

    private fun loginIfNot(): Boolean {
        if (userManager.isUserLogin()) {
            return true
        }
        toLogin()
        return false
    }

    fun openWebPage(jumpUrl: String) {
        BrowserStarter.newStarter(host)
            .withURL(jumpUrl)
            .start()
    }

}