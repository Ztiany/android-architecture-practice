import com.android.app.build.BuildConfig
import com.android.app.build.addComposeModuleDependencies

plugins {
    alias(libs.plugins.app.modularization.application)
    alias(libs.plugins.app.feature.compose)
}

android {
    defaultConfig {
        applicationId = "${BuildConfig.APPLICATION_ID}.${project.name.lowercase()}"
        versionCode = project.findProperty("app.version.code").toString().toInt()
        versionName = project.findProperty("app.version.name").toString()
    }
    namespace = "com.app.sample.compose"
    resourcePrefix = "sample_"
}

dependencies {
    implementation(project(":common:ui-compose"))
    project.addComposeModuleDependencies()
    implementation(libs.compose.paging.android)
}