package me.ztiany.architecture.account.api

import android.content.Context
import com.android.common.api.router.AppNavigator

interface AccountModuleNavigator : AppNavigator {

    fun openAccount(context: Context)

}