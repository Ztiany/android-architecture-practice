package me.ztiany.architecture.main.api

import android.content.Context
import com.android.common.router.Navigator

interface MainModuleNavigator : Navigator {

    fun openMain(context: Context)

    fun exitAndLogin(context: Context)

}