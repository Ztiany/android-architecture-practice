plugins {
    alias(libs.plugins.app.modularization.library)
    alias(libs.plugins.jetbrains.kotlin.kapt)
}

android {
    namespace = "me.ztiany.wan.main"
    resourcePrefix = "main_"
}

dependencies {
    implementation(project(":feature:main:api"))
    implementation(project(":feature:account:api"))

    kapt(libs.airbnb.epoxy.apt)
}