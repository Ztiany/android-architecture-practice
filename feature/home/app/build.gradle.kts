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
    implementation(project(":feature:home:main"))
    implementation(project(":feature:home:api"))
    implementation(project(":feature:account:api"))
}