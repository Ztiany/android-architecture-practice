package com.android.base.architecture.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.android.base.AndroidSword

open class BaseAppContext : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        AndroidSword.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AndroidSword.onCreate(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        AndroidSword.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        AndroidSword.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AndroidSword.onConfigurationChanged(newConfig)
    }

    override fun onTerminate() {
        super.onTerminate()
        AndroidSword.onTerminate()
    }

}