package me.ztiany.wan.account

import android.content.Context
import android.content.Intent
import me.ztiany.wan.account.api.AccountModuleNavigator

internal class AccountModuleNavigatorImpl : AccountModuleNavigator {

    override fun openLoginPage(context: Context, clearStack: Boolean) {
        context.startActivity(Intent(context, AccountActivity::class.java).apply {
            if (clearStack) {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        })
    }

}