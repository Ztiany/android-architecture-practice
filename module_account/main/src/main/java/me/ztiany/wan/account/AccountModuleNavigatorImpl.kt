package me.ztiany.wan.account

import android.content.Context
import android.content.Intent
import me.ztiany.wan.account.api.AccountModuleNavigator

internal class AccountModuleNavigatorImpl : AccountModuleNavigator {

    override fun openAccount(context: Context) {
        context.startActivity(Intent(context, AccountActivity::class.java))
    }

}