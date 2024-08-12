plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.ui.dialog"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // foundation
    implementation(libs.base.arch.utils)
    implementation(libs.base.arch.adapter)
    // androidx
    implementation(libs.androidx.annotations)
    implementation(libs.google.ui.material)
    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
    // theme
    implementation(project(":common:ui-theme"))
    // log
    implementation(libs.jakewharton.timber)
}