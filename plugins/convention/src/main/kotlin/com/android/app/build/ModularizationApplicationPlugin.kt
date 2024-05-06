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
            applicationId = "${BuildConfig.APPLICATION_ID}.${getName().lowercase()}"

            targetSdk = BuildConfig.TARGET_SDK_VERSION

            versionCode = BuildConfig.VERSION_CODE
            versionName = BuildConfig.VERSION_NAME
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
                storeFile = project.rootProject.file(BuildConfig.Signing.RELEASE_KEY_FILE_NAME)
                storePassword = BuildConfig.Signing.RELEASE_KEY_PASSWORD
                keyAlias = BuildConfig.Signing.RELEASE_KEY_ALIAS
                keyPassword = BuildConfig.Signing.RELEASE_KEY_PASSWORD
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