plugins {
    alias(libs.plugins.app.modularization.library)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "me.ztiany.wan.sample"
    resourcePrefix = "sample_"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
    ksp(libs.airbnb.epoxy.processor)
}