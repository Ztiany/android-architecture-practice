package com.android.app.build

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.addComposeModuleDependencies() {
    dependencies {
        add("implementation", libs.findLibrary("compose.activity").get())
        add("implementation", libs.findLibrary("compose.livedata").get())
        add("implementation", libs.findLibrary("compose.viewmodel").get())
        add("implementation", libs.findLibrary("compose.navigation").get())
        add("implementation", libs.findLibrary("compose.hilt.navigation").get())
        add("implementation", libs.findLibrary("compose.lifecycle.android").get())
    }
}