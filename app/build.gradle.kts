plugins {
    alias(libs.plugins.app.final.application)
}

android {
    namespace = "me.ztiany.wan.app"
}

dependencies {
    // modules
    implementation(project(":common:core"))
    implementation(project(":feature:main:main"))
    implementation(project(":feature:account:main"))

    // hilt
    api(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
}