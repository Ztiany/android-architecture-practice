plugins {
    alias(libs.plugins.app.modularization.application)
}

android {
    namespace = "me.ztiany.wan.sample.app"
    resourcePrefix = "sample_"
    defaultConfig {
        versionCode = project.findProperty("app.version.code").toString().toInt()
        versionName = project.findProperty("app.version.name").toString()
    }
}

dependencies {
    implementation(project(":sample:main"))
}