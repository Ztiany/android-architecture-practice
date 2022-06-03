package com.android.base.architecture.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.android.base.CrashProcessor
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Ztiany
 */
internal class ApplicationDelegate constructor() {

    private lateinit var application: Application

    private lateinit var crashHandler: CrashHandler

    private val onCreateCalled = AtomicBoolean(false)

    private val onAttachBaseCalled = AtomicBoolean(false)

    fun attachBaseContext(@Suppress("UNUSED_PARAMETER") base: Context) {
        check(onAttachBaseCalled.compareAndSet(false, true)) { "Can only be called once" }
    }

    fun onCreate(application: Application) {
        check(onCreateCalled.compareAndSet(false, true)) { "Can only be called once" }
        this.application = application
        //异常日志记录
        crashHandler = CrashHandler.register(application)
    }

    fun onTerminate() {
    }

    fun onConfigurationChanged(@Suppress("UNUSED_PARAMETER") newConfig: Configuration) {
    }

    fun onTrimMemory(@Suppress("UNUSED_PARAMETER") level: Int) {
    }

    fun onLowMemory() {
    }

    internal fun setCrashProcessor(crashProcessor: CrashProcessor) {
        crashHandler.setCrashProcessor(crashProcessor)
    }

}