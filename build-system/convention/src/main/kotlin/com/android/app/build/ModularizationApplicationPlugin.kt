package com.android.app.build

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * Used for setting up the application module of a feature module.
 */
class ModularizationApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configureBusinessModulePlugins(true)

        extensions.getByType<ApplicationExtension>().apply {
            configureAndroidModuleCommonOptions(
                isApplication = true,
                enableViewBinding = true
            )
            configureAndroidApplication(target)
        }

        configureCommonKotlinCompiler()
        addBusinessModuleDependencies()
    }

    private fun ApplicationExtension.configureAndroidApplication(project: Project) {
        defaultConfig {
            applicationId = "${BuildConfig.applicationId}.${getName().lowercase()}"

            targetSdk = BuildConfig.targetSdkVersion

            versionCode = BuildConfig.versionCode
            versionName = BuildConfig.versionName
        }

        packaging {
            jniLibs {
                pickFirsts += listOf(
                    "lib/x86/libc++_shared.so",
                    "lib/x86_64/libc++_shared.so",
                    "lib/armeabi-v7a/libc++_shared.so",
                    "lib/arm64-v8a/libc++_shared.so"
                )
            }
        }

        signingConfigs {
            create("release") {
                storeFile = project.rootProject.file(BuildConfig.Signing.releaseKeyFileName)
                storePassword = BuildConfig.Signing.releaseKeyPassword
                keyAlias = BuildConfig.Signing.releaseKeyAlias
                keyPassword = BuildConfig.Signing.releaseKeyPassword
            }
        }

        buildTypes {
            getByName("debug") {
                signingConfig = signingConfigs.getByName("release")
            }
            getByName("release") {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

}