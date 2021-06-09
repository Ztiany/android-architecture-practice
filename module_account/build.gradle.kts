apply {
    from("${rootProject.projectDir.absolutePath}/modularization_config.gradle.kts")
}

android {
	resourcePrefix("account_")
}
