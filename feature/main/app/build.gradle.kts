plugins {
    alias(libs.plugins.app.modularization.application)
}

android {
    namespace = "me.ztiany.wan.main.app"
    resourcePrefix = "main_"
}

dependencies {
    implementation(project(":feature:main:main"))
    implementation(project(":feature:main:api"))
    implementation(project(":feature:account:api"))
}