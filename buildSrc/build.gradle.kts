//refer to: https://github.com/gradle/kotlin-dsl-samples/issues/1287

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    maven { url = uri("https://maven.aliyun.com/repository/google") }
    maven { url = uri("https://dl.google.com/dl/android/maven2/") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    google()
    mavenCentral()
}

dependencies {
//    implementation("com.android.tools.build:gradle:4.1.3")
//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
//    implementation("com.google.dagger:hilt-android-gradle-plugin:2.35.1")
}