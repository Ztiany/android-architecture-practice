package com.android.app.build

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
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
    compileSdk = BuildConfig.compileSdkVersion

    defaultConfig {
        minSdk = BuildConfig.minSdkVersion

        vectorDrawables.useSupportLibrary = true
        resourceConfigurations += listOf("zh", "en")
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        if (!isApplication) {
            targetSdk = BuildConfig.targetSdkVersion
        }
    }

    lint {
        abortOnError = false
        if (!isApplication) {
            targetSdk = BuildConfig.targetSdkVersion
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
        JavaVersion.VERSION_11.toString(),
        listOf("-Xcontext-receivers")
    )
}

fun Project.configureKotlinCompiler(
    jvmTarget: String = JavaVersion.VERSION_11.toString(),
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
    // local libs
    add("implementation", fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

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
