plugins {
    alias(libs.plugins.app.common.library)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.hilt)
}

android {
    namespace = "com.app.base"

    defaultConfig {
        buildConfigField("String", "appHostFlag", "\"${project.findProperty("host_env")}\"")
        buildConfigField("boolean", "skipHttpCerVerifying", "${project.findProperty("skip_http_certificate_verifying")}")
    }

    buildFeatures {
        // If you don't want to generate the binding class of a layout, you can add tools:viewBindingIgnore="true" attribute to the root view.
        viewBinding = true
        buildConfig = true
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/debug/java")
            res.srcDir("src/debug/res")
        }
    }
}

dependencies {
    // testing
    testImplementation(libs.test.junit)

    // foundation
    api(libs.base.arch.core)
    api(libs.base.arch.fragment)
    api(libs.base.arch.utils)

    // performance monitor and debug
    api(project(":apm"))

    // app ui theme, dialog and widget and base business api implementation
    api(project(":common:ui-theme"))
    api(project(":common:ui-dialog"))
    api(project(":common:ui-widget"))
    api(project(":common:api"))
    api(project(":common:http"))

    // media selector
    api(project(":component:selector"))
    // upgrade component
    implementation(project(":component:upgrade"))

    // dependencies of specific feature api
    implementation(project(":feature:home:api"))

    // androidx
    api(libs.androidx.ktx)
    api(libs.androidx.exifinterface)
    api(libs.androidx.percentlayout)
    api(libs.google.ui.flexbox)
    compileOnly(libs.androidx.paging.runtime)

    // hilt
    implementation(libs.google.hilt)
    ksp(libs.google.hilt.compiler)

    // network
    implementation(libs.square.okhttp)
    implementation(libs.square.retrofit.converter.gson)
    implementation(libs.bumptech.glide)
    api(libs.google.gson)
    api(libs.square.retrofit)
    api(libs.ztiany.simplehttp)
    api(libs.ztiany.imageloader)
    kapt(libs.bumptech.glide.ksp)
    // utils
    api(libs.jooq.joor)
    api(libs.ztiany.livedataex)
    api(libs.ztiany.storage)
    api(libs.guolin.permissionx)
    // log
    api(libs.jakewharton.timber)
}