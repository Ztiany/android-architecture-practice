plugins {
    alias(libs.plugins.app.modularization.library)
    alias(libs.plugins.jetbrains.kotlin.kapt)
}

android {
    namespace = "me.ztiany.wan.sample"
    resourcePrefix = "sample_"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
    implementation(libs.square.okhttp.logging)
    kapt(libs.airbnb.epoxy.apt)
}