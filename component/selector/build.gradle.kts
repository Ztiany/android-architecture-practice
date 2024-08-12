plugins {
    alias(libs.plugins.app.common.library)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
}

android {
    namespace = "com.android.sdk.mediaselector"
}

dependencies {
    // androidx
    implementation(libs.androidx.annotations)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.android)
    // extension
    compileOnly(libs.google.gson)
    compileOnly(libs.ztiany.imageloader)
    compileOnly(libs.guolin.permissionx)
    compileOnly(libs.ztiany.archdelegate)
    /*
    Options:
        - [Matisse](https://github.com/leavesCZY/Matisse) (JetpackCompose)
        - [EasyPhotos](https://github.com/HuanTanSheng/EasyPhotos)
        - [PictureSelector](https://github.com/LuckSiege/PictureSelector)
        - [ImageSelector](https://github.com/smuyyh/ImageSelector)
     */
    implementation(libs.lucksiege.pictureselector)
    implementation(libs.lucksiege.compress)
    implementation(libs.lucksiege.ucrop)
    // log
    implementation(libs.jakewharton.timber)
}