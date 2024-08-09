package me.ztiany.wan.main.home

import android.content.Context
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

    private val host = context as MainActivity

    fun toLogin() {
        appRouter.getNavigator(AccountModuleNavigator::class.java)?.openAccount(host)
    }

}