package me.ztiany.wan.account.api

import android.content.Context
import com.app.common.api.router.AppNavigator

interface AccountModuleNavigator : AppNavigator {

    fun openAccount(context: Context)

}