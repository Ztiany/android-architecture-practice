plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.component.upgrade"
}

dependencies {
    // foundation
    implementation(libs.base.arch.utils)
    // androidx
    implementation(libs.androidx.annotations)
    implementation(libs.androidx.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    // kotlin
    implementation(libs.kotlin.stdlib)
    // library
    implementation(libs.square.okhttp)
    // log
    implementation(libs.jakewharton.timber)
}