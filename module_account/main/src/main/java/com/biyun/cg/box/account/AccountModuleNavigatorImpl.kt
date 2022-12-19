package com.biyun.cg.box.account

import android.content.Context
import android.content.Intent
import com.biyun.cg.box.account.api.AccountModuleNavigator

internal class AccountModuleNavigatorImpl : AccountModuleNavigator {

    override fun openAccount(context: Context) {
        context.startActivity(Intent(context, AccountActivity::class.java))
    }

}