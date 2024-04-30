package com.android.app.build

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * Used for setting up a common library module.
 *
 * Plugins applied:
 * - [BuildConfig.PluginId.ANDROID_LIB]
 * - [BuildConfig.PluginId.KOTLIN_ANDROID]
 *
 * Configures:
 * - Android compile options
 * - Kotlin compile options
 *
 * No dependencies are added.
 */
class CommonLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configureCommonLibraryModulePlugins()

        extensions.getByType<LibraryExtension>().configureAndroidModuleCommonOptions(
            isApplication = false,
            enableViewBinding = false
        )

        configureCommonKotlinCompiler()
    }

}