package me.ztiany.wan.account.api

import android.content.Context
import com.app.common.api.router.Navigator

interface AccountModuleNavigator : Navigator {

    fun openAccount(context: Context)

}