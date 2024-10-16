plugins {
    alias(libs.plugins.app.final.application)
}

android {
    namespace = "me.ztiany.wan.app"
    defaultConfig {
        versionCode = project.findProperty("app.version.code").toString().toInt()
        versionName = project.findProperty("app.version.name").toString()
    }
}

dependencies {
    implementation(project(":common:core"))
    implementation(project(":feature:home:main"))
    implementation(project(":feature:account:main"))

    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)
}