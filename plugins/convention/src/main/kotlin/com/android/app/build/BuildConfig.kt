package com.android.app.build

object BuildConfig {
    const val APPLICATION_ID = "me.ztiany.android"

    const val MIN_SDK_VERSION = 23
    const val TARGET_SDK_VERSION = 30
    const val COMPILE_SDK_VERSION = 33

    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0.0"

    object Env {
        const val HTTPS_TRUST_ALL = false
        const val OPEN_DEBUG = false
        const val SHOW_DEBUG_TOOLS = false
        const val OPEN_LOG = true

        /**
         *  - ENV_AUTO = "Auto"
         *  - ENV_TEST = "Test"
         *  - ENV_RELEASE = "Pro"
         */
        const val HOST_ENV = "Auto"
    }

    object Signing {
        const val RELEASE_KEY_ALIAS = "hm"
        const val RELEASE_KEY_PASSWORD = "666666"
        const val RELEASE_KEY_FILE_NAME = "Android.jks"
    }

    object PluginId {
        const val ANDROID_APP = "com.android.application"
        const val ANDROID_LIB = "com.android.library"
        const val KOTLIN_ANDROID = "kotlin-android"
        const val KOTLIN_KAPT = "kotlin-kapt"
        const val KOTLIN_PARCELIZE = "kotlin-parcelize"
        const val GOOGLE_KSP = "com.google.devtools.ksp"
        const val GOOGLE_HILT = "dagger.hilt.android.plugin"
    }

}