if (AppConfig.isMultiApp) {
    apply {
        from("${rootProject.projectDir.absolutePath}/modularization_app.gradle.kts.kts")
    }
} else {
    apply {
        from("${rootProject.projectDir.absolutePath}/modularization_lib.gradle.kts.kts")
    }
}