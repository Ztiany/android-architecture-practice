import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.android.app.build"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.build.android.plugin)
    compileOnly(libs.build.kotlin.plugin)
    compileOnly(libs.build.ksp.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        // modularization plugins
        register("ModularizationApplicationPlugin") {
            id = "com.app.modularization.application"
            implementationClass = "com.android.app.build.ModularizationApplicationPlugin"
        }
        register("ModularizationLibraryPlugin") {
            id = "com.app.modularization.library"
            implementationClass = "com.android.app.build.ModularizationLibraryPlugin"
        }
        register("ModularizationAPIPlugin") {
            id = "com.app.modularization.api"
            implementationClass = "com.android.app.build.ModularizationAPIPlugin"
        }
        // common plugin
        register("CommonLibraryPlugin") {
            id = "com.app.common.library"
            implementationClass = "com.android.app.build.CommonLibraryPlugin"
        }
        // final app plugin
        register("FinalApplicationPlugin") {
            id = "com.app.final.application"
            implementationClass = "com.android.app.build.FinalApplicationPlugin"
        }
    }
}
