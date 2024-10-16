plugins {
    alias(libs.plugins.app.common.library)
    alias(libs.plugins.app.feature.compose)
}

android {
    namespace = "com.app.base.compose"
}

dependencies {
    // theme
    implementation(project(":common:ui-theme"))
    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
    // compose extension
    implementation(libs.compose.paging.android)
    // log
    implementation(libs.jakewharton.timber)

    // arch
    api(libs.base.arch.core)
    api(libs.ztiany.uistate)
}