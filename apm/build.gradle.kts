plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.apm"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("boolean", "openLog", "${project.findProperty("app.debug.log")}")
        buildConfigField("boolean", "openDebugMode", "${project.findProperty("app.debug.tool")}")
        buildConfigField("boolean", "showDebugTool", "${project.findProperty("app.debug.tool")}")
    }
}

dependencies {
    // foundation
    implementation(libs.base.arch.utils)
    // androidx
    implementation(libs.androidx.annotations)
    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
    // log
    implementation(libs.jakewharton.timber)
    // library
    implementation(libs.jooq.joor)
    implementation(libs.square.okhttp)
    implementation(libs.square.okhttp.logging)
    implementation(libs.tencent.bugly)
    // library for debug
    if (project.findProperty("app.debug.tool").toString().toBoolean()) {
        // stetho
        implementation(libs.facebook.stetho)
        implementation(libs.facebook.stetho.okhttp3)
        // leakcanary
        implementation(libs.squareup.leakcanary.debug)
    }
}