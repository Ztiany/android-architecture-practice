package me.ztiany.architecture.account.apiimpl

import android.content.Context
import android.content.Intent
import com.google.auto.service.AutoService
import me.ztiany.architecture.account.AccountActivity
import me.ztiany.architecture.account.api.AccountModuleNavigator

@AutoService(AccountModuleNavigator::class)
class AccountModuleNavigatorImpl : AccountModuleNavigator {

    override fun openAccount(context: Context) {
        context.startActivity(Intent(context, AccountActivity::class.java))
    }

}