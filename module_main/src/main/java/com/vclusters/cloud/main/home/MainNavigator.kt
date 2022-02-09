package com.vclusters.cloud.main.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.app.base.router.AppRouter
import com.app.base.services.usermanager.UserManager
import com.vclusters.cloud.account.api.AccountModule
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-14 14:20
 */
@ActivityScoped
class MainNavigator @Inject constructor(
    @ActivityContext context: Context,
    private val appRouter: AppRouter,
    private val userManager: UserManager
) {

    private val host = context as AppCompatActivity

    fun toLogin() {
        appRouter.build(AccountModule.PATH).navigation()
    }

    fun toSwitchAccount() {
        appRouter.build(AccountModule.PATH)
            .withInt(AccountModule.ACTION_KEY,AccountModule.ACTION_SWITCH)
            .navigation()
    }

    fun openMessageCenter() {

    }

}