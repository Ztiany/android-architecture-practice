plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.ui.widget"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // theme
    implementation(project(":common:ui-theme"))
    // foundation
    implementation(libs.base.arch.utils)
    implementation(libs.base.arch.adapter)
    implementation(libs.base.arch.fragment)
    // androidx
    implementation(libs.androidx.annotations)
    implementation(libs.google.ui.material)
    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
    // log
    implementation(libs.jakewharton.timber)
}