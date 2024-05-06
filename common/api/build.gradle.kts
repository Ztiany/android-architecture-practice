plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.api"
}

dependencies {
    // androidx
    implementation (libs.androidx.annotations)
    
    // kotlin
    api (libs.kotlin.stdlib)
    api (libs.kotlin.reflect)
    api (libs.kotlinx.coroutines)
    api (libs.kotlinx.coroutines.android)

    // net
    compileOnly (libs.ztiany.simplehttp)
}