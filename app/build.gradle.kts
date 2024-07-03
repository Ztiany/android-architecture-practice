plugins {
    alias(libs.plugins.app.final.application)
}

android {
    namespace = "me.ztiany.wan.app"
    defaultConfig {
        versionCode = project.findProperty("version_code").toString().toInt()
        versionName = project.findProperty("version_name").toString()
    }
}

dependencies {
    // modules
    implementation(project(":common:core"))
    implementation(project(":feature:home:main"))
    implementation(project(":feature:account:main"))

    // hilt
    api(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
}