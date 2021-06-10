if (AppConfig.isMultiApp) {
    plugins {
        id("modularization_app")
    }
} else {
    plugins {
        id("modularization_lib")
    }
}