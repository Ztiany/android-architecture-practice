plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.ui.dialogdsl"
}

dependencies {
    // androidx
    implementation (libs.androidx.annotations)
    
    // kotlin
    api (libs.kotlin.stdlib)
    api (libs.kotlin.reflect)
    api (libs.kotlinx.coroutines)
    api (libs.kotlinx.coroutines.android)
}