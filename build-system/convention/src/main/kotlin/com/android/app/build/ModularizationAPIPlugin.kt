package com.android.app.build

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Used for setting up the API module of a feature module.
 */
class ModularizationAPIPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configureCommonLibraryModulePlugins()

        extensions.getByType<LibraryExtension>().configureAndroidModuleCommonOptions(
            isApplication = false,
            enableViewBinding = false
        )

        configureCommonKotlinCompiler()
        addDependencies()
    }

    private fun Project.addDependencies() = dependencies {
        // core api
        add("implementation", project(":common:api"))
        // android
        add("implementation", libs.findLibrary("androidx.annotations").get())
    }

}