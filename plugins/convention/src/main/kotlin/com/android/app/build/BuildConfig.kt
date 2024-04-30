package com.android.app.build

object BuildConfig {

    const val applicationId = "me.ztiany.android"

    const val minSdkVersion = 23
    const val targetSdkVersion = 30
    const val compileSdkVersion = 33

    const val versionCode = 1
    const val versionName = "1.0.0"

    const val enableProguard = false
    const val httpsTrustAll = false
    const val isOpenDebug = false
    const val showDebugTools = false
    const val isOpenLog = true

    /**
     *  - def ENV_AUTO = "Auto"
     *  - def ENV_TEST = "Test"
     *  - def ENV_RELEASE = "Pro"
     */
    const val HOST_ENV = "Auto"

    object Signing {
        const val releaseKeyAlias = "hm"
        const val releaseKeyPassword = "666666"
        const val releaseKeyFileName = "Android.jks"
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