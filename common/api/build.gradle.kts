plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.api"
}

dependencies {
    // androidx
    implementation(libs.androidx.annotations)
    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
    // net
    implementation(libs.ztiany.simplehttp)
}