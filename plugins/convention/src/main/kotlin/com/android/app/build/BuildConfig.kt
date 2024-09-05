package com.android.app.build

import org.gradle.api.JavaVersion

object BuildConfig {
    const val APPLICATION_ID = "me.ztiany.android"

    const val MIN_SDK_VERSION = 23
    const val TARGET_SDK_VERSION = 34
    const val COMPILE_SDK_VERSION = 34
    val JAVA_VERSION = JavaVersion.VERSION_17

    object Signing {
        const val RELEASE_KEY_ALIAS = "hm"
        const val RELEASE_KEY_PASSWORD = "666666"
        const val RELEASE_KEY_FILE_NAME = "Android.jks"
    }

    object PluginId {
        const val ANDROID_APP = "com.android.application"
        const val ANDROID_LIB = "com.android.library"
        const val KOTLIN_ANDROID = "kotlin-android"
        const val KOTLIN_PARCELIZE = "kotlin-parcelize"
        const val GOOGLE_KSP = "com.google.devtools.ksp"
        const val GOOGLE_HILT = "dagger.hilt.android.plugin"
    }

    object Compose {
        // Compose to Kotlin Compatibility Map: https://developer.android.com/jetpack/androidx/releases/compose-kotlin
        const val KOTLIN_COMPILER_EXTENSION_VERSION = "1.5.14"
    }
    
}