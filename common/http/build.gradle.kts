plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.component.merchant"
}

dependencies {
    implementation(libs.androidx.annotations)

    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)

    // log
    implementation(libs.jakewharton.timber)

    // core function
    api(libs.ztiany.simplehttp)
    api(libs.google.gson)
    api(libs.square.retrofit)
}