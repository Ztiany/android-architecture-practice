package me.ztiany.wan.main

import android.content.Context
import com.app.common.api.router.AppRouter
import com.app.common.api.usermanager.UserManager
import me.ztiany.wan.account.api.AccountModuleNavigator
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 *@author Ztiany
 */
@ActivityScoped
internal class MainInternalNavigator @Inject constructor(
    @ActivityContext context: Context,
    private val appRouter: AppRouter,
    private val userManager: UserManager,
) {

    private val host = context as MainActivity

    fun toLogin() {
        appRouter.getNavigator(AccountModuleNavigator::class.java)?.openAccount(host)
    }

}