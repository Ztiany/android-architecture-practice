package com.app.apm

import android.app.Application
import timber.log.Timber
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

}