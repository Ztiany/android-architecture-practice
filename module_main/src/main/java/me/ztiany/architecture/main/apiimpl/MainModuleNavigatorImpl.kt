package me.ztiany.architecture.main.apiimpl

import android.content.Context
import android.content.Intent
import me.ztiany.architecture.main.api.MainModuleNavigator
import me.ztiany.architecture.main.home.MainActivity

internal class MainModuleNavigatorImpl : MainModuleNavigator {

    override fun openMain(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }

    override fun exitAndLogin(context: Context) {
        // TODO: re-login
        context.startActivity(Intent(context, MainActivity::class.java))
    }

}