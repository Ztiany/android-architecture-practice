plugins {
    alias(libs.plugins.app.modularization.application)
}

android {
    namespace = "me.ztiany.wan.sample.app"
    resourcePrefix = "sample_"
    defaultConfig {
        versionCode = project.findProperty("version_code").toString().toInt()
        versionName = project.findProperty("version_name").toString()
    }
}

dependencies {
    implementation(project(":sample:main"))
}