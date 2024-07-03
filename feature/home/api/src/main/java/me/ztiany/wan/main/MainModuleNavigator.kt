package me.ztiany.wan.main

import android.content.Context
import com.app.common.api.router.AppNavigator

interface MainModuleNavigator : AppNavigator {

    fun openMain(context: Context)

    fun exitAndLogin(context: Context)

}