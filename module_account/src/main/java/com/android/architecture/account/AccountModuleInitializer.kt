package com.android.architecture.account

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.android.base.architecture.app.AppLifecycle
import com.google.auto.service.AutoService

@AutoService(AppLifecycle::class)
class AccountModuleInitializer : AppLifecycle {

    override fun attachBaseContext(base: Context) {
        Log.d(
            "AppLifecycle",
            "AccountModuleInitializer-attachBaseContext() called with: base = $base"
        )
    }

    override fun onCreate(application: Application) {
        Log.d(
            "AppLifecycle",
            "AccountModuleInitializer-onCreate() called with: application = $application"
        )
    }

    override fun onTerminate() {
    }

    override fun onTrimMemory(level: Int) {
    }

    override fun onLowMemory() {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }

}