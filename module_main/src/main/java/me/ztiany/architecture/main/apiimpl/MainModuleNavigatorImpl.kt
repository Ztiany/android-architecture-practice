package me.ztiany.architecture.main.apiimpl

import android.content.Context
import android.content.Intent
import com.google.auto.service.AutoService
import me.ztiany.architecture.main.api.MainModuleNavigator
import me.ztiany.architecture.main.home.MainActivity

@AutoService(MainModuleNavigator::class)
class MainModuleNavigatorImpl : MainModuleNavigator {

    override fun openMain(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }

    override fun exitAndLogin(context: Context) {
        // TODO: re-login
        context.startActivity(Intent(context, MainActivity::class.java))
    }

}