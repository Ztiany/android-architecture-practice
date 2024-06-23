plugins {
    alias(libs.plugins.app.modularization.application)
}

android {
    namespace = "me.ztiany.wan.sample.app"
    resourcePrefix = "sample_"
}

dependencies {
    implementation(project(":feature:sample:main"))
}