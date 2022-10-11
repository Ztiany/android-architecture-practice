package me.ztiany.architecture.main.api

import android.content.Context
import com.android.common.api.router.Navigator

interface MainModuleNavigator : Navigator {

    fun openMain(context: Context)

    fun exitAndLogin(context: Context)

}