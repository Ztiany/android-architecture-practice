package com.android.app.build

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ComposeFeaturePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        if (plugins.hasPlugin(BuildConfig.PluginId.ANDROID_APP)) {
            extensions.getByType<ApplicationExtension>().configureComposeFeature()
            addComposeDependencies()
        } else if (plugins.hasPlugin(BuildConfig.PluginId.ANDROID_LIB)) {
            extensions.getByType<LibraryExtension>().configureComposeFeature()
            addComposeDependencies()
        } else {
            throw IllegalStateException("ComposeFeaturePlugin can only be applied to Android application or library modules")
        }
    }

    private fun CommonExtension<*, *, *, *, *, *>.configureComposeFeature() {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = BuildConfig.Compose.KOTLIN_COMPILER_EXTENSION_VERSION
        }
    }

    private fun Project.addComposeDependencies() = dependencies {
        add("implementation", platform(libs.findLibrary("compose.bom").get()))

        add("implementation", libs.findLibrary("compose.ui").get())
        add("implementation", libs.findLibrary("compose.ui.graphics").get())
        add("implementation", libs.findLibrary("compose.ui.tooling.preview").get())
        add("implementation", libs.findLibrary("compose.material3").get())
        add("implementation", libs.findLibrary("compose.ui.constraint").get())

        add("implementation", libs.findLibrary("compose.animation").get())
        add("implementation", libs.findLibrary("compose.animation.core").get())
        add("implementation", libs.findLibrary("compose.animation.graphics").get())

        add("debugImplementation", libs.findLibrary("compose.ui.tooling").get())
        add("debugImplementation", libs.findLibrary("compose.ui.test.manifest").get())
    }

}