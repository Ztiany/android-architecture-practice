plugins {
    alias(libs.plugins.app.common.library)
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.app.base.component.uitask"
}

dependencies {
    // foundation
    implementation(libs.base.arch.utils)
    // android x
    implementation(libs.androidx.annotations)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ktx)
    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    // di
    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
    // log
    implementation(libs.jakewharton.timber)
}