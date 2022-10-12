package me.ztiany.architecture.main.api

import android.content.Context
import com.android.common.api.router.AppNavigator

interface MainModuleNavigator : AppNavigator {

    fun openMain(context: Context)

    fun exitAndLogin(context: Context)

}