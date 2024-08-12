plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.data.protocol"
}

dependencies {
    // androidx
    implementation(libs.androidx.annotations)
    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    // core function
    implementation(libs.ztiany.simplehttp)
    implementation(libs.google.gson)
    implementation(libs.square.retrofit)
    // log
    implementation(libs.jakewharton.timber)
}