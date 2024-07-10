import com.android.app.build.BuildConfig

plugins {
    alias(libs.plugins.app.common.library)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.hilt)
}

android {
    namespace = "com.app.base"

    defaultConfig {
        buildConfigField("boolean", "openDebug", "${project.findProperty("open_debug")}")
        buildConfigField("boolean", "openLog", "${project.findProperty("open_log")}")
        buildConfigField("boolean", "showDebugTools", "${project.findProperty("show_debug_tools")}")
        buildConfigField("boolean", "trustAllHttpsCertification", "${project.findProperty("https_trust_all")}")
        buildConfigField("String", "specifiedHost", "\"${project.findProperty("host_env")}\"")
    }

    //如果不想生成某个布局的绑定类，可以在根视图添加 tools:viewBindingIgnore="true" 属性。
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/common/java")
            res.srcDir("src/common/res")

            java.srcDir("src/tools-web/java")
            res.srcDir("src/tools-web/res")

            java.srcDir("src/tools-gallery/java")
            res.srcDir("src/tools-gallery/res")

            java.srcDir("src/tools-upgrade/java")
            res.srcDir("src/tools-upgrade/res")

            java.srcDir("src/tools-debug/java")
            res.srcDir("src/tools-debug/res")

            java.srcDir("src/tools-sdk/java")
        }
    }
}

dependencies {
    // 测试
    testImplementation(libs.test.junit)

    // 通用架构
    api(libs.base.arch.core)
    api(libs.base.arch.fragment)
    // 通用工具
    api(libs.base.arch.utils)
    // SDK API 规范
    api(project(":common:ui"))
    // SDK UI 规范
    api(project(":common:api"))
    // 性能监控
    implementation(project(":apm:core"))
    // 依赖的业务 API
    implementation(project(":feature:home:api"))

    // androidx
    api(libs.androidx.ktx)
    api(libs.androidx.exifinterface)
    api(libs.androidx.percentlayout)
    api(libs.google.ui.flexbox)
    compileOnly(libs.androidx.paging.runtime)

    // hilt
    api(libs.google.hilt)
    ksp(libs.google.hilt.compiler)

    // network
    implementation(libs.square.okhttp)
    implementation(libs.square.okhttp.logging)
    api(libs.ztiany.simplehttp)
    api(libs.google.gson)
    api(libs.square.retrofit)
    implementation(libs.square.retrofit.converter.gson)
    api(libs.ztiany.imageloader)
    kapt(libs.bumptech.glide.ksp)

    // ui
    api(libs.ztiany.mediaselector)
    api(libs.yslibrary.keyboardvisibilityevent)

    // utils
    api(libs.jooq.joor)
    api(libs.ztiany.livedataex)
    api(libs.ztiany.storage)
    api(libs.guolin.permissionx)
    implementation(libs.zelory.compressor)

    // debug
    if (project.findProperty("open_debug").toString().toBoolean()) {
        // stetho
        implementation(libs.facebook.stetho)
        implementation(libs.facebook.stetho.okhttp3)
        // leakcanary
        implementation(libs.squareup.leakcanary.debug)
    }
}