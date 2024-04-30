package com.android.app.build

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * Used for setting up the library module of a feature module.
 */
class ModularizationLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configureBusinessModulePlugins(false)

        extensions.getByType<LibraryExtension>().configureAndroidModuleCommonOptions(
            isApplication = false,
            enableViewBinding = true
        )

        configureCommonKotlinCompiler()
        addBusinessModuleDependencies()
    }

}