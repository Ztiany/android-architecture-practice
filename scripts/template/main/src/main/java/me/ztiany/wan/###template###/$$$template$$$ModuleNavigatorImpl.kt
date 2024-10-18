package me.ztiany.wan.###template###

import android.app.Activity
import android.content.Intent
import me.ztiany.wan.###template###.api.$$$template$$$ModuleNavigator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class $$$template$$$ModuleNavigatorImpl @Inject constructor() : $$$template$$$ModuleNavigator {

    override fun open$$$template$$$Module(activity: Activity) {
        activity.startActivity(Intent(activity, $$$template$$$Activity::class.java))
    }

}