import com.android.app.build.BuildConfig

plugins {
    alias(libs.plugins.app.modularization.application)
    alias(libs.plugins.google.ksp)
}

android {
    defaultConfig {
        applicationId = "${BuildConfig.APPLICATION_ID}.${project.name.lowercase()}"
        versionCode = project.findProperty("app.version.code").toString().toInt()
        versionName = project.findProperty("app.version.name").toString()
    }

    namespace = "me.ztiany.wan.sample"
    resourcePrefix = "sample_"
}

dependencies {
    implementation(libs.androidx.paging.runtime)
    implementation(libs.square.okhttp.logging)
    ksp(libs.airbnb.epoxy.processor)
}