package me.ztiany.architecture.main

import android.content.Context
import android.content.Intent
import me.ztiany.architecture.main.api.MainModuleNavigator
import me.ztiany.architecture.main.home.MainActivity

internal const val PAGE_KEY = "page_key"

internal const val ACTION_KEY = "action_key"

/**
 * 登录过期，重新登录
 */
internal const val ACTION_RE_LOGIN = -100

internal class MainModuleNavigatorImpl : MainModuleNavigator {

    override fun openMain(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }

    override fun exitAndLogin(context: Context) {
        // TODO: re-login
        context.startActivity(Intent(context, MainActivity::class.java))
    }

}