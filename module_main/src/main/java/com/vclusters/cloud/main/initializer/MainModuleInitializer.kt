package com.vclusters.cloud.main.initializer

import android.app.Application
import android.content.res.Configuration
import com.android.base.architecture.app.AppLifecycle
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainModuleInitializer @Inject constructor(

) : AppLifecycle {

    override fun onCreate(application: Application) {
        Timber.d("MainModuleInitializer-onCreate() called with: application = $application")
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