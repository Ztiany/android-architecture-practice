plugins {
    alias(libs.plugins.app.common.library)
    alias(libs.plugins.app.feature.compose)
}

android {
    namespace = "com.app.base.compose"
}

dependencies {
    // androidx
    implementation(libs.androidx.annotations)
    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
}