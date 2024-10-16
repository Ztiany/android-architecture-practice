plugins {
    alias(libs.plugins.app.modularization.application)
}

android {
    namespace = "me.ztiany.wan.main.app"
    resourcePrefix = "main_"
    defaultConfig {
        versionCode = project.findProperty("app.version.code").toString().toInt()
        versionName = project.findProperty("app.version.name").toString()
    }
}

dependencies {
    implementation(project(":feature:home:main"))
    implementation(project(":feature:home:api"))
    implementation(project(":feature:account:api"))
}