plugins {
    alias(libs.plugins.app.modularization.library)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "me.ztiany.wan.main"
    resourcePrefix = "main_"
}

dependencies {
    implementation(project(":feature:home:api"))
    implementation(project(":feature:account:api"))
    implementation(libs.androidx.paging.runtime)
    ksp(libs.airbnb.epoxy.processor)
}