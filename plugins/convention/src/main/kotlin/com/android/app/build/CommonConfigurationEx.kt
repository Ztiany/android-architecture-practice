package com.android.app.build

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.configureCommonLibraryModulePlugins() {
    with(pluginManager) {
        apply(BuildConfig.PluginId.ANDROID_LIB)
        apply(BuildConfig.PluginId.KOTLIN_ANDROID)
        apply(BuildConfig.PluginId.KOTLIN_PARCELIZE)
    }
}

fun Project.configureBusinessModulePlugins(
    isApplication: Boolean,
) {
    with(pluginManager) {
        if (isApplication) {
            apply(BuildConfig.PluginId.ANDROID_APP)
        } else {
            apply(BuildConfig.PluginId.ANDROID_LIB)
        }
        apply(BuildConfig.PluginId.KOTLIN_ANDROID)
        apply(BuildConfig.PluginId.KOTLIN_PARCELIZE)
        apply(BuildConfig.PluginId.GOOGLE_KSP)
        apply(BuildConfig.PluginId.GOOGLE_HILT)
    }
}

fun CommonExtension<*, *, *, *, *, *>.configureAndroidModuleCommonOptions(
    isApplication: Boolean,
    enableViewBinding: Boolean,
) {
    compileSdk = BuildConfig.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = BuildConfig.MIN_SDK_VERSION

        vectorDrawables.useSupportLibrary = true
        resourceConfigurations += listOf("zh", "en")
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        if (!isApplication) {
            targetSdk = BuildConfig.TARGET_SDK_VERSION
        }
    }

    lint {
        abortOnError = false
        if (!isApplication) {
            targetSdk = BuildConfig.TARGET_SDK_VERSION
        }
    }

    compileOptions {
        sourceCompatibility = BuildConfig.JAVA_VERSION
        targetCompatibility = BuildConfig.JAVA_VERSION
    }

    // 如果不想生成某个布局的绑定类，可以在根视图添加 tools:viewBindingIgnore="true" 属性。
    if (enableViewBinding) {
        buildFeatures {
            viewBinding = true
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

fun Project.configureCommonKotlinCompiler() {
    configureKotlinCompiler(
        BuildConfig.JAVA_VERSION.toString(),
        listOf("-Xcontext-receivers")
    )
}

fun Project.configureKotlinCompiler(
    jvmTarget: String = BuildConfig.JAVA_VERSION.toString(),
    freeCompilerArgs: List<String>,
) {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            this.jvmTarget = jvmTarget
            this.freeCompilerArgs += freeCompilerArgs
        }
    }
}

fun Project.addBusinessModuleDependencies() = dependencies {
    // core business
    add("implementation", project(":common:core"))

    // hilt
    add("implementation", libs.findLibrary("google.hilt").get())
    add("ksp", libs.findLibrary("google.hilt.compiler").get())

    // test
    add("testImplementation", libs.findLibrary("test.junit").get())
}

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
