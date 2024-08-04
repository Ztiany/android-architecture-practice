plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.ui.dialog"
}

dependencies {
    // androidx
    implementation (libs.androidx.annotations)
    implementation (libs.google.ui.material)

    // kotlin
    api (libs.kotlin.stdlib)
    api (libs.kotlin.reflect)
    api (libs.kotlinx.coroutines)
    api (libs.kotlinx.coroutines.android)
}