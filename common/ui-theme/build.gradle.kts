plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.app.base.ui.theme"
}

dependencies {
    // androidx
    implementation (libs.androidx.annotations)

    // 通用 UI
    api (libs.base.arch.view)
    api (libs.qmuiteam.qmui)
    api (libs.kyleduo.switchbutton)
}