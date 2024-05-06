plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.ui"
}

dependencies {
    // androidx
    implementation (libs.androidx.annotations)

    // 通用 UI
    api (libs.qmuiteam.qmui)
    api (libs.kyleduo.switchbutton)
    api (project(":base:view"))
}