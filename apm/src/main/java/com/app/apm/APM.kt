package com.app.apm

import android.app.Application
import com.android.base.utils.android.DebugUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joor.Reflect
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.concurrent.atomic.AtomicBoolean

object APM {

    private lateinit var app: Application

    private val initializationProtection = AtomicBoolean(false)

    private val startProtection = AtomicBoolean(false)

    fun init(application: Application): APM {
        if (initializationProtection.getAndSet(true)) {
            throw IllegalStateException("APM has already been initialized")
        }
        Timber.d("APM initialized")
        app = application
        return this
    }

    fun start() {
        if (!initializationProtection.get()) {
            throw IllegalStateException("APM has not been initialized")
        }
        if (startProtection.compareAndSet(false, true)) {
            Timber.d("APM started")
            realStart()
        } else {
            Timber.d("APM has already been started")
        }
    }

    private fun realStart() {
        installCrashReport()
        doIfLogEnabled {
            // installLogger
            Timber.plant(DebugTree())
            Timber.e("============================= Logger is activated =============================")
            DebugUtils.printSystemInfo()
        }
        doIfDebugMode {
            Timber.e("============================= Debug-Mode is activated =============================")
            DebugUtils.startStrictMode()
            // installStetho
            Reflect.on("com.facebook.stetho.Stetho").call("initializeWithDefaults", app)
        }
    }

    private fun installCrashReport() = runCatching {
        //val strategy = CrashReport.UserStrategy(app)
        //strategy.appVersion = AppUtils.getAppVersionName()
        //CrashReport.initCrashReport(app, "0eef6c2165", false, strategy)
    }

    fun stop() {
        if (!initializationProtection.get()) {
            return
        }
        if (startProtection.compareAndSet(true, false)) {
            Timber.d("APM stopped")
            doRealStop()
        } else {
            Timber.d("APM has already been stopped")
        }
    }

    private fun doRealStop() {

    }

    ///////////////////////////////////////////////////////////////////////////
    // api
    ///////////////////////////////////////////////////////////////////////////

    val debugToolEnabled: Boolean
        get() = BuildConfig.showDebugTools

    val logEnabled: Boolean
        get() = BuildConfig.openLog

    val debugMode: Boolean
        get() = BuildConfig.openDebug

    fun installStethoHttp(builder: OkHttpClient.Builder) {
        doIfDebugMode {
            val interceptor = Reflect.on("com.facebook.stetho.okhttp3.StethoInterceptor").create().get<Interceptor>()
            builder.addNetworkInterceptor(interceptor)
        }
    }

    fun installOkHttpLogging(builder: OkHttpClient.Builder, logger: (String) -> Unit) {
        APM.doIfLogEnabled {
            with(HttpLoggingInterceptor(logger)) {
                level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(this)
            }
        }
    }

}