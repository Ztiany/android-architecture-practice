package me.ztiany.architecture.account.api

import android.content.Context
import com.android.common.api.router.Navigator

interface AccountModuleNavigator : Navigator {

    fun openAccount(context: Context)

}