// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    println("buildscript in rootProject is called.")
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.parcelize) apply false
    alias(libs.plugins.jetbrains.kotlin.kapt) apply false
    alias(libs.plugins.google.hilt) apply false
    alias(libs.plugins.google.ksp) apply false

    alias(libs.plugins.app.modularization.application) apply false
    alias(libs.plugins.app.modularization.library) apply false
    alias(libs.plugins.app.modularization.api) apply false
    alias(libs.plugins.app.common.library) apply false
    alias(libs.plugins.app.final.application) apply false

    alias(libs.plugins.vanniktech.maven.publisher) apply false
}

allprojects {
    configurations.configureEach {
        resolutionStrategy.force(
            libs.androidx.appcompat,
            libs.androidx.activity,
            libs.androidx.activity.ktx,
            libs.androidx.fragment,
            libs.androidx.fragment.ktx,
            libs.androidx.ktx,
            libs.androidx.annotations,
            libs.androidx.constraintlayout,
            libs.kotlin.stdlib,
            libs.kotlinx.coroutines
        )
    }
}