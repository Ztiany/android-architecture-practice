plugins {
    alias(libs.plugins.app.modularization.application)
}

android {
    namespace = "me.ztiany.wan.main.app"
    resourcePrefix = "main_"
    defaultConfig {
        versionCode = project.findProperty("version_code").toString().toInt()
        versionName = project.findProperty("version_name").toString()
    }
}

dependencies {
    implementation(project(":feature:main:main"))
    implementation(project(":feature:main:api"))
    implementation(project(":feature:account:api"))
}