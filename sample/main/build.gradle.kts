import com.android.app.build.addComposeModuleDependencies

plugins {
    alias(libs.plugins.app.modularization.library)
    alias(libs.plugins.app.feature.compose)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "me.ztiany.wan.sample"
    resourcePrefix = "sample_"

    sourceSets {
        getByName("main") {
            java.srcDir("src/compose/java")
            res.srcDir("src/compose/res")
            java.srcDir("src/view/java")
            res.srcDir("src/view/res")
        }
    }
}

dependencies {
    implementation(project(":common:ui-compose"))
    project.addComposeModuleDependencies()

    implementation(libs.androidx.paging.runtime)
    ksp(libs.airbnb.epoxy.processor)
}