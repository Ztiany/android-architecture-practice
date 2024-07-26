package me.ztiany.wan.main

import android.content.Context
import com.app.common.api.router.Navigator

interface MainModuleNavigator : Navigator {

    fun openMain(context: Context)

    fun exitAndLogin(context: Context)

}